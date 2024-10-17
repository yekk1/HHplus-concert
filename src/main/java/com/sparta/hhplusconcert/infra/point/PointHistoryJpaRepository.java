package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistoryEntity,Long> {

}
