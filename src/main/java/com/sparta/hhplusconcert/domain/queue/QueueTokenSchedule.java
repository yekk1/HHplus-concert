package com.sparta.hhplusconcert.domain.queue;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.queue.entity.QueueTokenEntity;
import com.sparta.hhplusconcert.infra.queue.QueueTokenRepositoryImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueTokenSchedule {
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

  //TODO: 만료시간이 지난 대기열 토큰 상태->만료 처리

  //TODO: 임시 예약 만료 처리 (5분 뒤)
}
