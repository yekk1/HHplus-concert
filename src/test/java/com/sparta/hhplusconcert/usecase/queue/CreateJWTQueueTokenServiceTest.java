package com.sparta.hhplusconcert.usecase.queue;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
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

public class CreateJWTQueueTokenServiceTest {

  @Mock
  private QueueTokenRepositoryImpl queueTokenRepository;

  @InjectMocks
  private CreateJWTQueueTokenService createJWTQueueTokenService;

  private static final String SECRET_KEY = "concertSecretKey";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testCreateJWTQueueToken_Success() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTQueueTokenService.Input input = CreateJWTQueueTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    when(queueTokenRepository.save(any(QueueTokenEntity.class))).thenReturn(1L);

    // When
    CreateJWTQueueTokenService.Output output = createJWTQueueTokenService.create(input);

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
    verify(queueTokenRepository, times(1)).save(any(QueueTokenEntity.class));
  }

  @Test
  void testCreateJWTQueueToken_TokenHasCorrectValues() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTQueueTokenService.Input input = CreateJWTQueueTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    when(queueTokenRepository.save(any(QueueTokenEntity.class))).thenReturn(1L);

    // When
    CreateJWTQueueTokenService.Output output = createJWTQueueTokenService.create(input);

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
    verify(queueTokenRepository, times(1)).save(any(QueueTokenEntity.class));
  }

  @Test
  void testCreateJWTQueueToken_TokenSavingFails() {
    // Given
    UUID userUuid = UUID.randomUUID();
    CreateJWTQueueTokenService.Input input = CreateJWTQueueTokenService.Input.builder()
        .userUuid(userUuid)
        .build();

    // 저장 실패 시 0L 반환
    when(queueTokenRepository.save(any(QueueTokenEntity.class))).thenReturn(0L);

    // When
    CreateJWTQueueTokenService.Output output = createJWTQueueTokenService.create(input);

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
    verify(queueTokenRepository, times(1)).save(any(QueueTokenEntity.class));
  }
}