package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository{
  private final PaymentJpaRepository paymentJpaRepository;

  @Override
  public Long generatePayment(PaymentEntity payment) {
    PaymentEntity savedPayment = paymentJpaRepository.save(payment);
    return savedPayment.getId();
  }
}
