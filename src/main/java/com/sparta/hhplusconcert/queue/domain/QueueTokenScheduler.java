package com.sparta.hhplusconcert.queue.domain;

import com.sparta.hhplusconcert.queue.domain.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.queue.infra.QueueTokenRepositoryImpl;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueTokenScheduler {
  private final QueueTokenRepositoryImpl queueTokenRepository;

  //30초에 5명
  //접속 중인 인원이 50명 이상이면 대기
  @Scheduled(fixedRate = 30_000)
  public void pushQueue(){
    Long connectedUserCount = queueTokenRepository.countByStatus(Status.CONNECTED);
    if(connectedUserCount > 50) return;

    Pageable pageable = PageRequest.of(0, 5);
    List<QueueTokenEntity> queueTokenList = queueTokenRepository.findTopByStatusWaiting(Status.WAITING, pageable);
    List<Long> ids = queueTokenList.stream()
        .map(QueueTokenEntity::getId)
        .collect(Collectors.toList());

    if (!ids.isEmpty()) {
      Integer updatedCount = queueTokenRepository.pushQueue(ids);
      if (updatedCount > 0) {
        System.out.println(updatedCount + " records updated to CONNECTED.");
      } else {
        System.out.println("No records were updated.");
      }
    } else {
      System.out.println("No waiting tokens found.");
    }
  }

  //만료시간이 지난 대기열 토큰 상태->만료 처리
  @Transactional
  @Scheduled(fixedRate = 30_000)
  public void expireQueueToken(){
    //만료시간이 지난 토큰 가져옴
    List<QueueTokenEntity> expiredQueueTokens = queueTokenRepository.getExpiredQueueTokens(
        LocalDateTime.now());
    //예약취소로 업데이트
    if (!expiredQueueTokens.isEmpty()) {
      for (QueueTokenEntity expiredQueueToken : expiredQueueTokens) {
        expiredQueueToken.setStatus(Status.EXPIRED);
      }
      Integer savedTokensSize = queueTokenRepository.saveAll(expiredQueueTokens);
      if (savedTokensSize != expiredQueueTokens.size()){
        throw new RuntimeException("토큰 만료 처리를 실패했습니다.");
      }
    } else {
      System.out.println("기간 만료 된 토큰이 없습니다.");
    }
  }
}
