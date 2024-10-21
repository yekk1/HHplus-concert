package com.sparta.hhplusconcert.point.infra;

import com.sparta.hhplusconcert.point.domain.entity.PaymentEntity;

public interface PaymentRepository {

  Long generatePayment(PaymentEntity payment);

}
