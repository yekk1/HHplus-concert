package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository userJpaRepository;

  @Override
  public UserEntity getUserData(Long id){
    return userJpaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당하는 사용자가 존재하지 않습니다. ID: " + id));
  }

  @Override
  public Long userPoint(UserEntity user) {
    UserEntity savedUser = userJpaRepository.save(user);
    return savedUser.getId();
  }

  @Override
  public Long chargePoint(UserEntity user){
    UserEntity savedUser = userJpaRepository.save(user);
    return savedUser.getId();
  }

  @Override
  public Long getPoint(Long id){
    return userJpaRepository.findPointById(id);
  }
}
