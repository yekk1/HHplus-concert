package com.sparta.hhplusconcert.usecase.point;

import com.sparta.hhplusconcert.domain.point.PointTransactionType;
import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.domain.point.entity.UserEntity;
import com.sparta.hhplusconcert.infra.point.PointHistoryRepositoryImpl;
import com.sparta.hhplusconcert.infra.point.UserRepositoryImpl;
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
  private final UserRepositoryImpl userRepository;
  private final PointHistoryRepositoryImpl pointHistoryRepository;
  @Getter
  public static class Input {
    Long userId;
    Long amount;
  }
  @Data
  @Builder
  public static class Output {
    Long userId;
    Long pointHistoryId;
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
          .user_id(input.getUserId())
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
