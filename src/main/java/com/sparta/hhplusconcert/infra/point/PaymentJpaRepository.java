package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

}
