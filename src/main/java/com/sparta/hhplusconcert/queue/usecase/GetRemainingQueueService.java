package com.sparta.hhplusconcert.queue.usecase;

import com.sparta.hhplusconcert.queue.domain.Status;
import com.sparta.hhplusconcert.queue.domain.ExpiredTokenException;
import com.sparta.hhplusconcert.queue.domain.InvalidTokenException;
import com.sparta.hhplusconcert.queue.domain.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.queue.infra.QueueTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRemainingQueueService {
  @Qualifier("QueueToken")
  private final QueueTokenRepositoryImpl queueTokenRepository;

  public Integer get(String token) {
    QueueTokenEntity queueToken = queueTokenRepository.check(token);

    if (queueToken == null) {
      throw new InvalidTokenException("유효한 토큰이 아닙니다.");
    }

    // 토큰이 대기 중 상태면 남은 순번 계산
    if (queueToken.getStatus() == Status.WAITING) {
      long remainingQueue = queueTokenRepository.countRemainingQueue(queueToken.getIssuedTime(), Status.WAITING);
      return (int) remainingQueue; // 남은 순번 반환
    }

    // 토큰이 접속 완료 상태면 0 반환
    if (queueToken.getStatus() == Status.CONNECTED) {
      return 0; // 접속 완료로 간주
    }
    throw new ExpiredTokenException("토큰이 만료되었습니다.");
  }
}
