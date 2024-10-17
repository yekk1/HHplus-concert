package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PaymentEntity;

public interface PaymentRepository {

  Long generatePayment(PaymentEntity payment);

}
