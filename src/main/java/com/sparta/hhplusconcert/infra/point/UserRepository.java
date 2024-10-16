package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.UserEntity;

public interface UserRepository {
  UserEntity getUserData(Long id);

  Long chargePoint(UserEntity userEntity);
}
