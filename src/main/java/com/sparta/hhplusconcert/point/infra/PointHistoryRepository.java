package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;

public interface PointHistoryRepository {
  Long chargePoint(PointHistoryEntity pointHistory);
}
