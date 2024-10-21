package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.UserEntity;

public interface UserRepository {
  UserEntity getUserData(Long id);

  Long userPoint(UserEntity userEntity);

  Long chargePoint(UserEntity userEntity);

  Long getPoint(Long id);

}
