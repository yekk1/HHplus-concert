package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.InvalidTokenException;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepository;
import com.sparta.hhplusconcert.common.exception.TokenErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRemainingWaitingService {

  private final WaitingTokenRepository waitingTokenRepository;

  public Integer get(String token) {
    WaitingTokenEntity waitingToken = waitingTokenRepository.check(token);

    if (waitingToken == null) {
      throw new InvalidTokenException(TokenErrorCode.INVALID_WAITING_TOKEN);
    }

    // 토큰이 대기 중 상태면 남은 순번 계산
    if (waitingToken.getStatus() == Status.WAITING) {
      long remainingQueue = waitingTokenRepository.countRemainingQueue(waitingToken.getIssuedTime(), Status.WAITING);
      return (int) remainingQueue; // 남은 순번 반환
    }

    // 토큰이 접속 완료 상태면 0 반환
    if (waitingToken.getStatus() == Status.CONNECTED) {
      return 0; // 접속 완료로 간주
    }
    throw new InvalidTokenException(TokenErrorCode.EXPIRED_WAITING_TOKEN);
  }
}
