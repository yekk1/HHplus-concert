package com.sparta.hhplusconcert.usecase.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CheckQueueTokenServiceTest {

  @Mock
  private QueueTokenRepositoryImpl queueTokenRepository;

  @InjectMocks
  private CheckQueueTokenService checkQueueTokenService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testCheck_QueuePassed() {
    // Given
    String token = "testToken";
    UUID userUuid = UUID.randomUUID();
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.CONNECTED)
        .expiredTime(LocalDateTime.now().plusHours(3))
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Boolean result = checkQueueTokenService.check(token);
    assertThat(result).isTrue();
  }

  @Test
  void testCheck_QueueNotPassed() {
    // Given
    String token = "testToken";
    UUID userUuid = UUID.randomUUID();
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.WAITING)
        .expiredTime(LocalDateTime.now().plusHours(3))
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Boolean result = checkQueueTokenService.check(token);
    assertThat(result).isFalse();
  }

  @Test
  void testCheck_TokenExpired() {
    // Given
    String token = "testToken";
    UUID userUuid = UUID.randomUUID();
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.EXPIRED)
        .expiredTime(LocalDateTime.now().minusHours(3))
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Boolean result = checkQueueTokenService.check(token);
    assertThat(result).isFalse();
  }

  @Test
  void testCheck_TokenExpiredTime() {
    // Given
    String token = "testToken";
    UUID userUuid = UUID.randomUUID();
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.CONNECTED)
        .expiredTime(LocalDateTime.now().minusHours(3))
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Boolean result = checkQueueTokenService.check(token);
    assertThat(result).isFalse();
  }
}