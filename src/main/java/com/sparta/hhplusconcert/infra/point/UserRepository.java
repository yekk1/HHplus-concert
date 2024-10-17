package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.UserEntity;

public interface UserRepository {
  UserEntity getUserData(Long id);

  Long userPoint(UserEntity userEntity);

  Long chargePoint(UserEntity userEntity);

  Long getPoint(Long id);

}
