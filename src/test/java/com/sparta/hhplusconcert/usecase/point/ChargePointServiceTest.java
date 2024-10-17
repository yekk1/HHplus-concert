package com.sparta.hhplusconcert.usecase.point;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.domain.point.entity.UserEntity;
import com.sparta.hhplusconcert.infra.point.PointHistoryRepositoryImpl;
import com.sparta.hhplusconcert.infra.point.UserRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class ChargePointServiceTest {

  @InjectMocks
  private ChargePointService chargePointService;

  @Mock
  private UserRepositoryImpl userRepository;

  @Mock
  private PointHistoryRepositoryImpl pointHistoryRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testChargePointSuccess() {
    // given
    UUID userUuid = UUID.randomUUID();
    ChargePointService.Input input = ChargePointService.Input.builder()
        .userId(1L)
        .amount(100L)
        .build();

    UserEntity mockUser = UserEntity.builder()
        .id(1L)
        .userUuid(userUuid)
        .point(500L)
        .build();

    when(userRepository.getUserData(1L)).thenReturn(mockUser);
    when(userRepository.chargePoint(any(UserEntity.class))).thenReturn(1L);
    when(pointHistoryRepository.chargePoint(any(PointHistoryEntity.class))).thenReturn(1L);

    // when
    ChargePointService.Output output = chargePointService.charge(input);

    // then
    assertThat(output).isNotNull();
    assertThat(output.getUserId()).isEqualTo(1L);
    assertThat(output.getPointHistoryId()).isEqualTo(1L);

    // Verify interactions
    verify(userRepository).getUserData(1L);
    verify(userRepository).chargePoint(any(UserEntity.class));
    verify(pointHistoryRepository).chargePoint(any(PointHistoryEntity.class));
  }

  @Test
  void testChargePointFailure_UserNotFound() {
    Long userId = 1L;
    // given
    ChargePointService.Input input = ChargePointService.Input.builder()
        .userId(userId)
        .amount(100L)
        .build();

    // Mock the case where user is not found
    when(userRepository.getUserData(userId)).thenThrow(new EntityNotFoundException("해당하는 사용자가 존재하지 않습니다. ID: " + userId));

    // when, then
    assertThatThrownBy(() -> chargePointService.charge(input))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("해당하는 사용자가 존재하지 않습니다. ID: " + 1L);

    // Verify interactions
    verify(userRepository).getUserData(1L);
    verify(userRepository, never()).chargePoint(any(UserEntity.class));
    verify(pointHistoryRepository, never()).chargePoint(any(PointHistoryEntity.class));
  }
}