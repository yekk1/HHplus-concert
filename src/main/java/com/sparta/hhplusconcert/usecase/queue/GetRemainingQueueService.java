package com.sparta.hhplusconcert.usecase.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRemainingQueueService {
  private final QueueTokenRepositoryImpl queueTokenRepository;

  public Integer get(String token) {
    QueueTokenEntity queueToken = queueTokenRepository.check(token);

    // 토큰이 없으면 null 반환
    if (queueToken == null) {
      return null; // 토큰이 존재하지 않으면 만료로 간주
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

    // 그 외의 상태는 만료된 것으로 간주하고 null 반환
    return null;
  }
}
