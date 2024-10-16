package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository{
  private final PointHistoryJpaRepository pointHistoryJpaRepository;

  @Override
  public Long chargePoint(PointHistoryEntity pointHistory){
    PointHistoryEntity savedPointHistory = pointHistoryJpaRepository.save(pointHistory);
    return savedPointHistory.getId();
  }
}
