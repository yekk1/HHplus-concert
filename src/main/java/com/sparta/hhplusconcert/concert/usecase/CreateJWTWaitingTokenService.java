package com.sparta.hhplusconcert.concert.usecase;


import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.WaitingTokenScheduler;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateJWTWaitingTokenService {
  private final WaitingTokenRepository waitingTokenRepository;
  private final WaitingTokenScheduler waitingTokenScheduler;
  @Getter
  @Builder
  public static class Input{
    private UUID userUuid;
  }

  @Data
  @Builder
  public static class Output{
    private String token;
  }

  private static final String SECRET_KEY = "concertSecretKey";

  public Output create(Input input) {
    //로직

    //User데이터 받아와서 Jwt 토큰 생성
    JwtBuilder builder = Jwts.builder()
        .setSubject(input.getUserUuid().toString())
        .setHeader(createHeader())
        .setClaims(createClaims(input.getUserUuid()))
        .signWith(SignatureAlgorithm.HS256, createSigningKey())
        ;
    String token = builder.compact();

    //Jwt토큰으로 큐토큰엔티티 생성
    WaitingTokenEntity waitingToken = generatewaitingToken(token);
    //저장
    Long savedId = saveToken(waitingToken);

    //대기 순번 없으면 한 번 내보내기
    waitingTokenScheduler.pushQueue();

    //Jwt토큰 반환
    return Output.builder().token(token).build();
  }
  private Long saveToken(WaitingTokenEntity waitingToken) {
    Long saveId = waitingTokenRepository.save(waitingToken);
    return saveId;
  }
  private WaitingTokenEntity generatewaitingToken(String token) {

    return WaitingTokenEntity.builder()
        .userId(UUID.fromString(getUserUuidFromToken(token)))
        .token(token)
        .status(Status.WAITING)
        .issuedTime(getIssuedTimeFromToken(token))
        .expiredTime(getExpiredTimeFromToken(token))
        .build();
  }

  private static Claims getClaimsFormToken(String token) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(token).getBody();
  }

  private static String getUserUuidFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return claims.get("sub").toString();
  }
  private static LocalDateTime getExpiredTimeFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return timeStampToLocalDateTime((Long)claims.get("exp"));
  }

  private static LocalDateTime timeStampToLocalDateTime(Long timestamp) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    return localDateTime;
  }
  private static LocalDateTime getIssuedTimeFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return timeStampToLocalDateTime((Long) claims.get("iat"));
  }
  private static Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();

    header.put("typ", "JWT");
    header.put("alg", "HS256");
    header.put("regDate", System.currentTimeMillis());

    return header;
  }

  private static Map<String, Object> createClaims(UUID userUuid) {
    Map<String, Object> claims = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();

    claims.put("sub", userUuid.toString());
    claims.put("iat", now.toInstant(ZoneOffset.UTC).toEpochMilli());
    claims.put("exp", now.plusHours(3).toInstant(ZoneOffset.UTC).toEpochMilli());

    return claims;
  }

  private static Key createSigningKey() {
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
  }

}
