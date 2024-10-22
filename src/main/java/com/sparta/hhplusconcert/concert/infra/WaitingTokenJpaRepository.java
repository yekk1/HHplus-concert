package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.Status;
import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingTokenJpaRepository extends JpaRepository<WaitingTokenEntity, Long> {
  WaitingTokenEntity findAllByToken(String token);

  Long countByIssuedTimeBeforeAndStatus(LocalDateTime IssuedTime, Status status);

  Long countByStatus(Status status);

//  List<waitingTokenEntity> findTop50ByStatusOrderByIssuedTimeAsc(String status);

  @Query("SELECT q FROM WaitingTokenEntity q WHERE q.status = :status ORDER BY q.issuedTime ASC")
  List<WaitingTokenEntity> findTopByStatusOrderByIssuedTimeAsc(@Param("status") Status status, Pageable pageable);

  @Modifying
  @Transactional
  @Query("UPDATE WaitingTokenEntity q SET q.status = 'CONNECTED' WHERE q.id IN :ids")
  Integer updateStatusToConnected(List<Long> ids);

  @Query("SELECT q FROM WaitingTokenEntity q WHERE q.status != 'EXPIRED' AND q.expiredTime < :currentTime")
  List<WaitingTokenEntity> findExpiredTokens(@Param("currentTime") LocalDateTime currentTime);

}
