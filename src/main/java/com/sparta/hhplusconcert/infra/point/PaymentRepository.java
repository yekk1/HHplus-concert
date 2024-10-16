package com.sparta.hhplusconcert.infra.point;

import com.sparta.hhplusconcert.domain.point.entity.PaymentEntity;

public interface PaymentRepository {

  Integer generatePayment(PaymentEntity payment);

}
