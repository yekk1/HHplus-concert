package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Primary
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository {
  private final WaitingTokenJpaRepository waitingTokenJpaRepository;
  @Override
  public Long save(WaitingTokenEntity waitingToken){
    WaitingTokenEntity savedwaitingToken = waitingTokenJpaRepository.save(waitingToken);
    return savedwaitingToken.getId();
  }

  @Override
  public Integer saveAll(List<WaitingTokenEntity> waitingTokens) {
    List<WaitingTokenEntity> savedTokens = waitingTokenJpaRepository.saveAll(waitingTokens);
    return savedTokens.size();
  }

  @Override
  public WaitingTokenEntity check(String token){
    return waitingTokenJpaRepository.findAllByToken(token);
  }

  @Override
  public Long countRemainingQueue(LocalDateTime issuedTime, Status status) {
    return waitingTokenJpaRepository.countByIssuedTimeBeforeAndStatus(issuedTime, status);
  }

  @Override
  public Long countByStatus(Status status) {
    return waitingTokenJpaRepository.countByStatus(status);
  }

  @Override
  public List<WaitingTokenEntity> findTopByStatusWaiting(Status status, Pageable pageable) {
    return waitingTokenJpaRepository.findTopByStatusOrderByIssuedTimeAsc(status, pageable);
  }

  @Override
  public Integer pushQueue(List<Long> ids) {
    return waitingTokenJpaRepository.updateStatusToConnected(ids);
  }

  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public List<WaitingTokenEntity> getExpiredwaitingTokens(LocalDateTime currentTime) {
    return waitingTokenJpaRepository.findExpiredTokens(currentTime);
  }
}
