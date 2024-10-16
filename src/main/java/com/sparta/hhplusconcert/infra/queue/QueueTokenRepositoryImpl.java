package com.sparta.hhplusconcert.infra.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
}
