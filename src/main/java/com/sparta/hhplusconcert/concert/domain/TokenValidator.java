package com.sparta.hhplusconcert.concert.domain;

import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepository;
import com.sparta.hhplusconcert.exception.TokenErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidator {
  @Qualifier("WaitingToken")
  private final WaitingTokenRepository waitingTokenRepository;
  public Boolean isValid(String token) {
    WaitingTokenEntity waitingToken = waitingTokenRepository.check(token);

    if (waitingToken == null) {
      throw new InvalidTokenException(TokenErrorCode.INVALID_TOKEN);
    }
    if(waitingToken.isExpired()) {
      throw new InvalidTokenException(TokenErrorCode.EXPIRED_WAITING_TOKEN);
    }
    if(waitingToken.isTokenPassedQueue()) {
      return true;
    }
    throw new InvalidTokenException(TokenErrorCode.INVALID_TOKEN);
  }
}
