package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepositoryImpl;
import com.sparta.hhplusconcert.concert.usecase.GetRemainingWaitingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetRemainingWaitingServiceTest {

  @Mock
  private WaitingTokenRepositoryImpl waitingTokenRepository;

  @InjectMocks
  private GetRemainingWaitingService getRemainingWaitingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testGet_TokenWaitingState() {
    // Given
    String token = "testToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .status(Status.WAITING)
        .issuedTime(LocalDateTime.now().minusMinutes(10))
        .build();

    // When
    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);
    when(waitingTokenRepository.countRemainingQueue(waitingToken.getIssuedTime(), Status.WAITING)).thenReturn(5L);

    // Then
    Integer result = getRemainingWaitingService.get(token);
    assertThat(result).isEqualTo(5);
  }

  @Test
  void testGet_TokenConnectedState() {
    // Given
    String token = "testToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .status(Status.CONNECTED)
        .build();

    // When
    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // Then
    Integer result = getRemainingWaitingService.get(token);
    assertThat(result).isEqualTo(0);
  }

  @Test
  void testGet_TokenNotFound() {
    // Given
    String token = "nonExistentToken";

    // When
    when(waitingTokenRepository.check(token)).thenReturn(null);

    // Then
    Integer result = getRemainingWaitingService.get(token);
    assertThat(result).isNull();
  }

  @Test
  void testGet_TokenExpiredState() {
    // Given
    String token = "testToken";
    WaitingTokenEntity waitingToken = WaitingTokenEntity.builder()
        .token(token)
        .status(Status.EXPIRED)
        .build();

    // When
    when(waitingTokenRepository.check(token)).thenReturn(waitingToken);

    // Then
    Integer result = getRemainingWaitingService.get(token);
    assertThat(result).isNull();  // 만료된 경우 null을 반환하는지 확인
  }
}