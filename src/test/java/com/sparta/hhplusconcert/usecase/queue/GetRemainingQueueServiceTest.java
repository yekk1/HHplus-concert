package com.sparta.hhplusconcert.usecase.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetRemainingQueueServiceTest {

  @Mock
  private QueueTokenRepositoryImpl queueTokenRepository;

  @InjectMocks
  private GetRemainingQueueService getRemainingQueueService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testGet_TokenWaitingState() {
    // Given
    String token = "testToken";
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .status(Status.WAITING)
        .issuedTime(LocalDateTime.now().minusMinutes(10))
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);
    when(queueTokenRepository.countRemainingQueue(queueToken.getIssuedTime(), Status.WAITING)).thenReturn(5L);

    // Then
    Integer result = getRemainingQueueService.get(token);
    assertThat(result).isEqualTo(5);
  }

  @Test
  void testGet_TokenConnectedState() {
    // Given
    String token = "testToken";
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .status(Status.CONNECTED)
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Integer result = getRemainingQueueService.get(token);
    assertThat(result).isEqualTo(0);
  }

  @Test
  void testGet_TokenNotFound() {
    // Given
    String token = "nonExistentToken";

    // When
    when(queueTokenRepository.check(token)).thenReturn(null);

    // Then
    Integer result = getRemainingQueueService.get(token);
    assertThat(result).isNull();
  }

  @Test
  void testGet_TokenExpiredState() {
    // Given
    String token = "testToken";
    QueueTokenEntity queueToken = QueueTokenEntity.builder()
        .token(token)
        .status(Status.EXPIRED)
        .build();

    // When
    when(queueTokenRepository.check(token)).thenReturn(queueToken);

    // Then
    Integer result = getRemainingQueueService.get(token);
    assertThat(result).isNull();  // 만료된 경우 null을 반환하는지 확인
  }
}