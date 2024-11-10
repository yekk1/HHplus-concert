package com.sparta.hhplusconcert.point.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.point.infra.UserRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GetPointServiceUnitTest {

  @Mock
  private UserRepositoryImpl userRepository;

  @InjectMocks
  private GetPointService getPointService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Mockito 초기화
  }

  @Test
  void 현재_포인트를__조회할_수_있다() {
    // given
    Long userId = 1L;
    GetPointService.Input input = GetPointService.Input.builder()
        .UserId(userId)
        .build();
    Long expectedPoint = 1000L;

    when(userRepository.getPoint(userId)).thenReturn(expectedPoint);

    // when
    GetPointService.Output result = getPointService.get(input);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getPoint()).isEqualTo(expectedPoint);

    // verify
    verify(userRepository, times(1)).getPoint(userId);
  }

  @Test
  void 존재하지_않는_사용자면__조회에_실패() {
    // given
    Long userId = 1L;
    GetPointService.Input input = GetPointService.Input.builder()
        .UserId(userId)
        .build();

    when(userRepository.getPoint(userId)).thenThrow(new EntityNotFoundException("해당하는 사용자가 존재하지 않습니다. ID: " + userId));

    // when, then
    assertThatThrownBy(() -> getPointService.get(input))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("해당하는 사용자가 존재하지 않습니다. ID: " + 1L);

    // verify
    verify(userRepository).getPoint(1L);
  }
}