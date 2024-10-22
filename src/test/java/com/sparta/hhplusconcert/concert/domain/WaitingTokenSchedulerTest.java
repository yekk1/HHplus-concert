package com.sparta.hhplusconcert.concert.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sparta.hhplusconcert.concert.domain.WaitingTokenScheduler;
import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

public class WaitingTokenSchedulerTest {

  @InjectMocks
  private WaitingTokenScheduler waitingTokenScheduler;

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void pushQueue_whenConnectedUsersExceedsLimit_shouldNotUpdateQueue() {
    // Given
    when(waitingTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(51L);

    // When
    waitingTokenScheduler.pushQueue();

    // Then
    verify(waitingTokenRepository, never()).findTopByStatusWaiting(any(), any());
  }

  @Test
  void pushQueue_whenWaitingTokensAvailable_shouldUpdateTokensToConnected() {
    // Given
    when(waitingTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(30L);
    when(waitingTokenRepository.findTopByStatusWaiting(Status.WAITING, PageRequest.of(0, 5)))
        .thenReturn(List.of(
            WaitingTokenEntity.builder().id(1L).build(), WaitingTokenEntity.builder().id(2L).build()));

    // When
    waitingTokenScheduler.pushQueue();

    // Then
    verify(waitingTokenRepository).pushQueue(List.of(1L, 2L));
  }

  @Test
  void pushQueue_whenNoWaitingTokens_shouldLogNoTokensFound() {
    // Given
    when(waitingTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(30L);
    when(waitingTokenRepository.findTopByStatusWaiting(Status.WAITING, PageRequest.of(0, 5)))
        .thenReturn(Collections.emptyList());

    // When
    waitingTokenScheduler.pushQueue();

    // Then
    verify(waitingTokenRepository, never()).pushQueue(any());
  }

  @Test
  void expirewaitingToken_whenExpiredTokensPresent_shouldUpdateToExpired() {
    // Given
    LocalDateTime issuedTime = LocalDateTime.now();
    WaitingTokenEntity expiredToken = WaitingTokenEntity.builder().id(1L).issuedTime(issuedTime).build();
    expiredToken.setStatus(Status.WAITING);
    when(waitingTokenRepository.getExpiredwaitingTokens(issuedTime))
        .thenReturn(List.of(expiredToken));

    // When
    waitingTokenScheduler.expirewaitingToken();

    // Then
    assertThat(expiredToken.getStatus()).isEqualTo(Status.EXPIRED);
    verify(waitingTokenRepository).saveAll(List.of(expiredToken));
  }

  @Test
  void expirewaitingToken_whenNoExpiredTokens_shouldLogNoExpiredTokens() {
    // Given
    when(waitingTokenRepository.getExpiredwaitingTokens(LocalDateTime.now()))
        .thenReturn(Collections.emptyList());

    // When
    waitingTokenScheduler.expirewaitingToken();

    // Then
    verify(waitingTokenRepository, never()).saveAll(any());
  }

  @Test
  void expirewaitingToken_whenSaveFails_shouldThrowRuntimeException() {
    // Given
    WaitingTokenEntity expiredToken = WaitingTokenEntity.builder().id(1L).build();
    expiredToken.setStatus(Status.WAITING);
    when(waitingTokenRepository.getExpiredwaitingTokens(LocalDateTime.now()))
        .thenReturn(List.of(expiredToken));
    when(waitingTokenRepository.saveAll(any())).thenReturn(0); // Simulate failure

    // When & Then
    assertThatThrownBy(() -> waitingTokenScheduler.expirewaitingToken())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("토큰 만료 처리를 실패했습니다.");
  }
}