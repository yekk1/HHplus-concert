package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.common.exception.TokenErrorCode;
import com.sparta.hhplusconcert.concert.domain.InvalidTokenException;
import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.TokenValidator;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class TokenValidatorUnitTest {

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @InjectMocks
  private TokenValidator tokenValidator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void 유효한_토큰이면__참을_반납한다() {
    // Given
    String token = "testValidToken";
    UUID userUuid = UUID.randomUUID();
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.CONNECTED)
        .expiredTime(LocalDateTime.now().plusHours(3))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When
    // Then
    Boolean result = tokenValidator.isValid(token);
    assertThat(result).isTrue();
  }

  @Test
  void 유효한_토큰이_아니면__예외를_발생시킨다() {
    // Given
    String token = "testInvalidToken";
    UUID userUuid = UUID.randomUUID();
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.WAITING)
        .expiredTime(LocalDateTime.now().plusHours(3))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When & Then
    assertThatThrownBy(() -> tokenValidator.isValid(token))
        .isInstanceOf(RuntimeException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.INVALID_WAITING_TOKEN);
  }

  @Test
  void 존재하는_토큰이_아니면__예외를_발생시킨다() {
    // Given
    String token = "nonexistentToken";
    when(waitingTokenRepository.check(token)).thenReturn(null);

    // When & Then
    assertThatThrownBy(() -> tokenValidator.isValid(token))
        .isInstanceOf(RuntimeException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.INVALID_WAITING_TOKEN);
  }

  @Test
  void 만료된_토큰이면__예외를_발생시킨다() {
    // Given
    String token = "testExpiredToken";
    UUID userUuid = UUID.randomUUID();
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.EXPIRED)
        .expiredTime(LocalDateTime.now().minusHours(3))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When & Then
    assertThatThrownBy(() -> tokenValidator.isValid(token))
        .isInstanceOf(InvalidTokenException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.EXPIRED_WAITING_TOKEN);
  }

  @Test
  void 만료_시간이_지난_토큰이면__예외를_발생시킨다() {
    // Given
    String token = "testExpiredToken";
    UUID userUuid = UUID.randomUUID();
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .userId(userUuid)
        .status(Status.CONNECTED)
        .expiredTime(LocalDateTime.now().minusHours(3))
        .build();

    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // When & Then
    assertThatThrownBy(() -> tokenValidator.isValid(token))
        .isInstanceOf(InvalidTokenException.class)
        .extracting("errorCode")
        .isEqualTo(TokenErrorCode.EXPIRED_WAITING_TOKEN);
  }
}