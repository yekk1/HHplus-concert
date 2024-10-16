package com.sparta.hhplusconcert.infra.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long> {
  QueueTokenEntity findAllByToken(String token);

  Long countByIssuedTimeBeforeAndStatus(LocalDateTime IssuedTime, Status status);

  Long countByStatus(Status status);

//  List<QueueTokenEntity> findTop50ByStatusOrderByIssuedTimeAsc(String status);

  @Query("SELECT q FROM QueueTokenEntity q WHERE q.status = :status ORDER BY q.issuedTime ASC")
  List<QueueTokenEntity> findTopByStatusOrderByIssuedTimeAsc(@Param("status") Status status, Pageable pageable);

  @Modifying
  @Transactional
  @Query("UPDATE QueueTokenEntity q SET q.status = 'CONNECTED' WHERE q.id IN :ids")
  Integer updateStatusToConnected(List<Long> ids);

}
