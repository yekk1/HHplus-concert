package com.sparta.hhplusconcert.queue.infra;

import com.sparta.hhplusconcert.queue.domain.Status;
import com.sparta.hhplusconcert.queue.domain.entity.QueueTokenEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QueueTokenRepository {
  Long save(QueueTokenEntity queueToken);
  Integer saveAll(List<QueueTokenEntity> queueTokens);
  QueueTokenEntity check(String token);
  Long countRemainingQueue(LocalDateTime issuedTime, Status status);
  Long countByStatus(Status status);
  List<QueueTokenEntity> findTopByStatusWaiting(Status status, Pageable pageable);
  Integer pushQueue(List<Long> ids);
  List<QueueTokenEntity> getExpiredQueueTokens(LocalDateTime currentTime);
}
