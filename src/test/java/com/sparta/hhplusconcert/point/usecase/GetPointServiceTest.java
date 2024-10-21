package com.sparta.hhplusconcert.point.usecase;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.hhplusconcert.point.infra.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GetPointServiceTest {

  @Mock
  private UserRepositoryImpl userRepository;

  @InjectMocks
  private GetPointService getPointService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Mockito 초기화
  }

  @Test
  void testGetPoint() {
    // Given
    Long userId = 1L;
    Long expectedPoint = 1000L;

    when(userRepository.getPoint(userId)).thenReturn(expectedPoint);

    // When
    GetPointService.Input input = GetPointService.Input.builder().UserId(userId).build();
    GetPointService.Output result = getPointService.get(input);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getPoint()).isEqualTo(expectedPoint);

    // Verify that the repository was called exactly once
    verify(userRepository, times(1)).getPoint(userId);
  }
}