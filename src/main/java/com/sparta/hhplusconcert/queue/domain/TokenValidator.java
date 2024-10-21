package com.sparta.hhplusconcert.queue.domain;

import com.sparta.hhplusconcert.queue.domain.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.queue.infra.QueueTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidator {
  private final QueueTokenRepositoryImpl queueTokenRepository;
  public Boolean isValid(String token) {
    QueueTokenEntity queueToken = queueTokenRepository.check(token);

    if (queueToken == null) {
      throw new InvalidTokenException("유효한 토큰이 아닙니다.");
    }
    if(queueToken.isExpired()) {
      throw new ExpiredTokenException("토큰이 만료되었습니다.");
    }
    if(queueToken.isTokenPassedQueue()) {
      return true;
    }
    throw new InvalidTokenException("유효한 토큰이 아닙니다.");
  }
}
