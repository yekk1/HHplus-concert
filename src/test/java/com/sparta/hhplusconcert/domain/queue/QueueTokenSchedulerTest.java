package com.sparta.hhplusconcert.domain.queue;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

public class QueueTokenSchedulerTest {

  @InjectMocks
  private QueueTokenScheduler queueTokenScheduler;

  @Mock
  private QueueTokenRepositoryImpl queueTokenRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void pushQueue_whenConnectedUsersExceedsLimit_shouldNotUpdateQueue() {
    // Given
    when(queueTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(51L);

    // When
    queueTokenScheduler.pushQueue();

    // Then
    verify(queueTokenRepository, never()).findTopByStatusWaiting(any(), any());
  }

  @Test
  void pushQueue_whenWaitingTokensAvailable_shouldUpdateTokensToConnected() {
    // Given
    when(queueTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(30L);
    when(queueTokenRepository.findTopByStatusWaiting(Status.WAITING, PageRequest.of(0, 5)))
        .thenReturn(List.of(QueueTokenEntity.builder().id(1L).build(), QueueTokenEntity.builder().id(2L).build()));

    // When
    queueTokenScheduler.pushQueue();

    // Then
    verify(queueTokenRepository).pushQueue(List.of(1L, 2L));
  }

  @Test
  void pushQueue_whenNoWaitingTokens_shouldLogNoTokensFound() {
    // Given
    when(queueTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(30L);
    when(queueTokenRepository.findTopByStatusWaiting(Status.WAITING, PageRequest.of(0, 5)))
        .thenReturn(Collections.emptyList());

    // When
    queueTokenScheduler.pushQueue();

    // Then
    verify(queueTokenRepository, never()).pushQueue(any());
  }

  @Test
  void expireQueueToken_whenExpiredTokensPresent_shouldUpdateToExpired() {
    // Given
    QueueTokenEntity expiredToken = QueueTokenEntity.builder().id(1L).build();
    expiredToken.setStatus(Status.WAITING);
    when(queueTokenRepository.getExpiredQueueTokens(LocalDateTime.now()))
        .thenReturn(List.of(expiredToken));

    // When
    queueTokenScheduler.expireQueueToken();

    // Then
    assertThat(expiredToken.getStatus()).isEqualTo(Status.EXPIRED);
    verify(queueTokenRepository).saveAll(List.of(expiredToken));
  }

  @Test
  void expireQueueToken_whenNoExpiredTokens_shouldLogNoExpiredTokens() {
    // Given
    when(queueTokenRepository.getExpiredQueueTokens(LocalDateTime.now()))
        .thenReturn(Collections.emptyList());

    // When
    queueTokenScheduler.expireQueueToken();

    // Then
    verify(queueTokenRepository, never()).saveAll(any());
  }

  @Test
  void expireQueueToken_whenSaveFails_shouldThrowRuntimeException() {
    // Given
    QueueTokenEntity expiredToken = QueueTokenEntity.builder().id(1L).build();
    expiredToken.setStatus(Status.WAITING);
    when(queueTokenRepository.getExpiredQueueTokens(LocalDateTime.now()))
        .thenReturn(List.of(expiredToken));
    when(queueTokenRepository.saveAll(any())).thenReturn(0); // Simulate failure

    // When & Then
    assertThatThrownBy(() -> queueTokenScheduler.expireQueueToken())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("토큰 만료 처리를 실패했습니다.");
  }
}