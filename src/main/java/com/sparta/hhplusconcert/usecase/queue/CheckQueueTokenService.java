package com.sparta.hhplusconcert.usecase.queue;

import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckQueueTokenService {
  private final QueueTokenRepositoryImpl queueTokenRepository;
  public Boolean check(String token) {
    QueueTokenEntity queueToken = queueTokenRepository.check(token);
    return queueToken.isTokenPassedQueue() && !queueToken.isExpired();
  }
}