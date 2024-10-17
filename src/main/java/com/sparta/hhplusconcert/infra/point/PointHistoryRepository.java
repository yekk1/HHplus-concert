package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;

public interface PointHistoryRepository {
  Long chargePoint(PointHistoryEntity pointHistory);
}
