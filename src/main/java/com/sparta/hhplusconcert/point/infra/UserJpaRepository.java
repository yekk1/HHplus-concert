package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  Long findPointById(Long id);
}
