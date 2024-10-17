package com.sparta.hhplusconcert.usecase.queue;


import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateJWTQueueTokenService {

  private final QueueTokenRepositoryImpl queueTokenRepository;
  @Getter
  @Builder
  public static class Input{
    UUID userUuid;
  }

  @Data
  @Builder
  public static class Output{
    String token;
  }

  private static final String SECRET_KEY = "concertSecretKey";

  public Output create(Input input) {
    //로직

    //User데이터 받아와서 Jwt 토큰 생성
    JwtBuilder builder = Jwts.builder()
        .setSubject(input.getUserUuid().toString())
        .setHeader(createHeader())
        .setClaims(createClaims(input.getUserUuid().toString()))
        .signWith(SignatureAlgorithm.HS256, createSigningKey())
        ;
    String token = builder.compact();

    //Jwt토큰으로 큐토큰엔티티 생성
    QueueTokenEntity queueToken = generateQueueToken(token);
    //저장
    Long savedId = saveToken(queueToken);
    //Jwt토큰 반환

    return Output.builder().token(token).build();
  }
  private Long saveToken(QueueTokenEntity queueToken) {
    Long saveId = queueTokenRepository.save(queueToken);
    return saveId;
  }
  private QueueTokenEntity generateQueueToken(String token) {


    return QueueTokenEntity.builder()
        .userId(getUserUuidFromToken(token))
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

  private static UUID getUserUuidFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return (UUID) claims.get("sub");
  }
  private static LocalDateTime getExpiredTimeFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return (LocalDateTime) claims.get("exp");
  }

  private static LocalDateTime getIssuedTimeFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return (LocalDateTime) claims.get("iat");
  }
  private static Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();

    header.put("typ", "JWT");
    header.put("alg", "HS256");
    header.put("regDate", System.currentTimeMillis());

    return header;
  }

  private static Map<String, Object> createClaims(String userUuid) {
    Map<String, Object> claims = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();

    claims.put("sub", userUuid);
    claims.put("iat", now);
    claims.put("exp", now.plusHours(3));

    return claims;
  }

  private static Key createSigningKey() {
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
  }

}
