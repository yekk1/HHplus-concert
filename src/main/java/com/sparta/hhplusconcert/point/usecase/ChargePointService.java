package com.sparta.hhplusconcert.point.usecase;

import com.sparta.hhplusconcert.point.domain.PointTransactionType;
import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.point.infra.PointHistoryRepository;
import com.sparta.hhplusconcert.point.infra.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargePointService {
  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;
  @Getter
  @Builder
  public static class Input {
    private Long userId;
    private Long amount;
  }
  @Data
  @Builder
  public static class Output {
    private Long userId;
    private Long pointHistoryId;
  }
  @Transactional
  public Output charge(Input input){
      UserEntity user = userRepository.getUserData(input.getUserId());

      UserEntity userToSave = UserEntity.builder()
          .id(user.getId())
          .userUuid(user.getUserUuid())
          .point(user.getPoint() + input.getAmount())
          .build();
      Long savedUserId = userRepository.chargePoint(userToSave);

      PointHistoryEntity pointHistory = PointHistoryEntity.builder()
          .userId(input.getUserId())
          .amount(input.getAmount())
          .type(PointTransactionType.CHARGE)
          .build();
      Long savedHistoryId = pointHistoryRepository.chargePoint(pointHistory);
    return Output.builder()
        .userId(savedUserId)
        .pointHistoryId(savedHistoryId)
        .build();
  }
}
