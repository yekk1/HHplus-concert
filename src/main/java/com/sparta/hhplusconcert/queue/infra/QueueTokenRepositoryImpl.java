package com.sparta.hhplusconcert.queue.infra;

import com.sparta.hhplusconcert.queue.domain.Status;
import com.sparta.hhplusconcert.queue.domain.entity.QueueTokenEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository{
  private final QueueTokenJpaRepository queueTokenJpaRepository;
  @Override
  public Long save(QueueTokenEntity queueToken){
    QueueTokenEntity savedQueueToken = queueTokenJpaRepository.save(queueToken);
    return savedQueueToken.getId();
  }

  @Override
  public Integer saveAll(List<QueueTokenEntity> queueTokens) {
    List<QueueTokenEntity> savedTokens = queueTokenJpaRepository.saveAll(queueTokens);
    return savedTokens.size();
  }

  @Override
  public QueueTokenEntity check(String token){
    return queueTokenJpaRepository.findAllByToken(token);
  }

  @Override
  public Long countRemainingQueue(LocalDateTime issuedTime, Status status) {
    return queueTokenJpaRepository.countByIssuedTimeBeforeAndStatus(issuedTime, status);
  }

  @Override
  public Long countByStatus(Status status) {
    return queueTokenJpaRepository.countByStatus(status);
  }

  @Override
  public List<QueueTokenEntity> findTopByStatusWaiting(Status status, Pageable pageable) {
    return queueTokenJpaRepository.findTopByStatusOrderByIssuedTimeAsc(status, pageable);
  }

  @Override
  public Integer pushQueue(List<Long> ids) {
    return queueTokenJpaRepository.updateStatusToConnected(ids);
  }

  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public List<QueueTokenEntity> getExpiredQueueTokens(LocalDateTime currentTime) {
    return queueTokenJpaRepository.findExpiredTokens(currentTime);
  }
}
