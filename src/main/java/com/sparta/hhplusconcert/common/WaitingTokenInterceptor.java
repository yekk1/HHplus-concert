package com.sparta.hhplusconcert.common;

import com.sparta.hhplusconcert.common.exception.TokenErrorCode;
import com.sparta.hhplusconcert.concert.domain.InvalidTokenException;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class WaitingTokenInterceptor implements HandlerInterceptor {

  private final WaitingTokenRepository waitingTokenRepository;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("X-WAITING-TOKEN");
    if (token == null) {
      throw new InvalidTokenException(TokenErrorCode.INVALID_WAITING_TOKEN);
    }
    WaitingTokenEntity waitingToken = waitingTokenRepository.check(token);

    if (waitingToken == null) {
      throw new InvalidTokenException(TokenErrorCode.INVALID_WAITING_TOKEN);
    }
    if(waitingToken.isExpired()) {
      throw new InvalidTokenException(TokenErrorCode.EXPIRED_WAITING_TOKEN);
    }
    if(waitingToken.isTokenPassedQueue()) {
      return true;
    }
    throw new InvalidTokenException(TokenErrorCode.INVALID_WAITING_TOKEN);
  }

}
