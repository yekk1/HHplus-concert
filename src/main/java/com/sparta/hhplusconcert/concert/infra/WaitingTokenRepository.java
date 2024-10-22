package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface WaitingTokenRepository {
  Long save(WaitingTokenEntity waitingToken);
  Integer saveAll(List<WaitingTokenEntity> waitingTokens);
  WaitingTokenEntity check(String token);
  Long countRemainingQueue(LocalDateTime issuedTime, Status status);
  Long countByStatus(Status status);
  List<WaitingTokenEntity> findTopByStatusWaiting(Status status, Pageable pageable);
  Integer pushQueue(List<Long> ids);
  List<WaitingTokenEntity> getExpiredwaitingTokens(LocalDateTime currentTime);
}
