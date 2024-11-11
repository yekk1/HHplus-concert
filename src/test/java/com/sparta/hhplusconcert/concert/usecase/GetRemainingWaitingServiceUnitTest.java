package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.common.exception.TokenErrorCode;
import com.sparta.hhplusconcert.concert.domain.InvalidTokenException;
import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class GetRemainingWaitingServiceUnitTest {

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @InjectMocks
  private GetRemainingWaitingService getRemainingWaitingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void 토큰의_대기상태면_순번을_반환한다() {
    // Given
    String token = "testWaitingToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .id(1L)
        .userId(UUID.randomUUID())
        .token(token)
        .status(Status.WAITING)
        .issuedTime(LocalDateTime.now().minusMinutes(10))
        .expiredTime(LocalDateTime.now().plusMinutes(10))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);
    when(waitingTokenRepository.countRemainingQueue(waitingToken.getIssuedTime(), Status.WAITING)).thenReturn(5L);

    // When
    Integer result = getRemainingWaitingService.get(token);

    //Then
    assertThat(result).isEqualTo(5);
  }

  @Test
  void 토큰이_유효한_상태면__0을_반환한다() {
    // Given
    String token = "testValidToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .id(1L)
        .userId(UUID.randomUUID())
        .token(token)
        .status(Status.CONNECTED)
        .issuedTime(LocalDateTime.now().minusMinutes(10))
        .expiredTime(LocalDateTime.now().plusMinutes(10))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When
    Integer result = getRemainingWaitingService.get(token);

    // Then
    assertThat(result).isEqualTo(0);
  }

  @Test
  void 존재하지_않는_토큰이면__예외를_발생시킨다() {
    // Given
    String token = "nonExistentToken";

    when(waitingTokenRepository.check(token)).thenReturn(null);

    // When & Then
    assertThatThrownBy(() -> getRemainingWaitingService.get(token))
        .isInstanceOf(InvalidTokenException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.INVALID_WAITING_TOKEN);
  }

  @Test
  void 만료된_토큰이면___예외를_발생시킨다() {
    // Given
    String token = "testExpiredToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .id(1L)
        .userId(UUID.randomUUID())
        .token(token)
        .status(Status.EXPIRED)
        .issuedTime(LocalDateTime.now().minusMinutes(15))
        .expiredTime(LocalDateTime.now().minusMinutes(5))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When & Then
    assertThatThrownBy(() -> getRemainingWaitingService.get(token))
        .isInstanceOf(InvalidTokenException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.EXPIRED_WAITING_TOKEN);
  }
}