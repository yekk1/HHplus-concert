package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Qualifier("PointHistory")
public class PointHistoryRepositoryImpl implements PointHistoryRepository{
  private final PointHistoryJpaRepository pointHistoryJpaRepository;

  @Override
  public Long chargePoint(PointHistoryEntity pointHistory){
    PointHistoryEntity savedPointHistory = pointHistoryJpaRepository.save(pointHistory);
    return savedPointHistory.getId();
  }
}
