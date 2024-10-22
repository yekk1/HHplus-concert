package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import com.sparta.hhplusconcert.concert.usecase.CreateJWTWaitingTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CreateJWTWaitingTokenServiceTest {

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @InjectMocks
  private CreateJWTWaitingTokenService createJWTWaitingTokenService;

  private static final String SECRET_KEY = "concertSecretKey";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testCreateJWTwaitingToken_Success() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTWaitingTokenService.Input input = CreateJWTWaitingTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    when(waitingTokenRepository.save(any(WaitingTokenEntity.class))).thenReturn(1L);

    // When
    CreateJWTWaitingTokenService.Output output = createJWTWaitingTokenService.create(input);

    // Then
    assertThat(output).isNotNull();
    assertThat(output.getToken()).isNotEmpty();

    // JWT 토큰 검증
    Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(output.getToken())
        .getBody();

    assertThat(claims.getSubject()).isEqualTo(userUuid.toString());
    assertThat(claims.get("iat")).isNotNull();
    assertThat(claims.get("exp")).isNotNull();

    // 저장 로직 호출 여부 검증
    verify(waitingTokenRepository, times(1)).save(any(WaitingTokenEntity.class));
  }

  @Test
  void testCreateJWTwaitingToken_TokenHasCorrectValues() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTWaitingTokenService.Input input = CreateJWTWaitingTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    when(waitingTokenRepository.save(any(WaitingTokenEntity.class))).thenReturn(1L);

    // When
    CreateJWTWaitingTokenService.Output output = createJWTWaitingTokenService.create(input);

    // Then
    assertThat(output).isNotNull();
    assertThat(output.getToken()).isNotEmpty();

    // JWT 토큰의 subject가 userUuid와 동일한지 검증
    Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(output.getToken())
        .getBody();

    assertThat(claims.getSubject()).isEqualTo(userUuid.toString());

    // 만료 시간과 발급 시간이 올바르게 설정되어 있는지 검증
    Instant instantIat = Instant.ofEpochMilli((Long)claims.get("iat"));
    LocalDateTime issuedAt = LocalDateTime.ofInstant(instantIat, ZoneId.systemDefault());
    Instant instantExp = Instant.ofEpochMilli((Long)claims.get("exp"));
    LocalDateTime expiration = LocalDateTime.ofInstant(instantExp, ZoneId.systemDefault());

    assertThat(issuedAt).isNotNull();
    assertThat(expiration).isNotNull();
    assertThat(expiration.isAfter(issuedAt)).isTrue();

    // 저장 로직 검증
    verify(waitingTokenRepository, times(1)).save(any(WaitingTokenEntity.class));
  }

  @Test
  void testCreateJWTwaitingToken_TokenSavingFails() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTWaitingTokenService.Input input = CreateJWTWaitingTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    // 저장 실패 시 0L 반환
    when(waitingTokenRepository.save(any(WaitingTokenEntity.class))).thenReturn(0L);

    // When
    CreateJWTWaitingTokenService.Output output = createJWTWaitingTokenService.create(input);

    // Then
    assertThat(output).isNotNull();
    assertThat(output.getToken()).isNotEmpty();

    // JWT 토큰 검증
    Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(output.getToken())
        .getBody();

    assertThat(claims.getSubject()).isEqualTo(userUuid.toString());

    // 저장 로직 검증
    verify(waitingTokenRepository, times(1)).save(any(WaitingTokenEntity.class));
  }
}