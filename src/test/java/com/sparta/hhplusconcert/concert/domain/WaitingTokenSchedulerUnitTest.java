package com.sparta.hhplusconcert.concert.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

public class WaitingTokenSchedulerUnitTest {

  @InjectMocks
  private WaitingTokenScheduler waitingTokenScheduler;

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void 접속_인원이_제한_인원을_초과하면__대기열을_업데이트하지_않는다() {
    // Given
    when(waitingTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(51L);

    // When
    waitingTokenScheduler.pushQueue();

    // Then
    verify(waitingTokenRepository, never()).findTopByStatusWaiting(any(), any());
  }

  @Test
  void 대기중인_토큰이_있으면__대기열을_업데이트한다() {
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
  void 대기중인_토큰이_없으면__대기열을_업데이트하지_않는다() {
    // Given
    when(waitingTokenRepository.countByStatus(Status.CONNECTED)).thenReturn(30L);
    when(waitingTokenRepository.findTopByStatusWaiting(Status.WAITING, PageRequest.of(0, 5)))
        .thenReturn(Collections.emptyList());

    // When & Then
    waitingTokenScheduler.pushQueue();

    // Then
    verify(waitingTokenRepository, never()).pushQueue(any());
  }

  @Test
  void 만료_시간이_지난_토큰이_있으면__상태를_만료로_업데이트한다() {
    // Given
    LocalDateTime issuedTime = LocalDateTime.now().minusMinutes(15);
    LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(5);
    WaitingTokenEntity expiredToken = WaitingTokenEntity.builder()
        .id(1L)
        .userId(UUID.randomUUID())
        .token("token")
        .issuedTime(issuedTime)
        .expiredTime(expiredTime)
        .status(Status.WAITING)
        .build();

    when(waitingTokenRepository.getExpiredwaitingTokens(any(LocalDateTime.class)))
        .thenReturn(List.of(expiredToken));

    // When
    waitingTokenScheduler.expirewaitingToken();

    // Then
    assertThat(expiredToken.getStatus()).isEqualTo(Status.EXPIRED);

    // Verify
    verify(waitingTokenRepository).saveAll(List.of(expiredToken));
  }

  @Test
  void 만료_시간이_지난_토큰이_없으면__상태를_업데이트하지_않는다() {
    // Given
    when(waitingTokenRepository.getExpiredwaitingTokens(LocalDateTime.now()))
        .thenReturn(Collections.emptyList());

    // When
    waitingTokenScheduler.expirewaitingToken();

    // Verify
    verify(waitingTokenRepository, never()).saveAll(any());
  }
}