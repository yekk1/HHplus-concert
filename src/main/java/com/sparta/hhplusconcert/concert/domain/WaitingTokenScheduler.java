package com.sparta.hhplusconcert.concert.domain;

import com.sparta.hhplusconcert.concert.domain.entity.WaitingTokenEntity;
import com.sparta.hhplusconcert.concert.infra.WaitingTokenRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingTokenScheduler {

  private final WaitingTokenRepository waitingTokenRepository;

  //30초에 5명
  //접속 중인 인원이 50명 이상이면 대기
  @Scheduled(fixedRate = 30_000)
  public void pushQueue(){
    Long connectedUserCount = waitingTokenRepository.countByStatus(Status.CONNECTED);
    if(connectedUserCount > 50){
      log.info("활성화 된 토큰이 접속 제한 수를 초과하여 대기열을 업데이트 하지 않았습니다.");
      return;
    }

    Pageable pageable = PageRequest.of(0, 5);
    List<WaitingTokenEntity> waitingTokenList = waitingTokenRepository.findTopByStatusWaiting(Status.WAITING, pageable);
    List<Long> ids = waitingTokenList.stream()
        .map(WaitingTokenEntity::getId)
        .collect(Collectors.toList());

    if (!ids.isEmpty()) {
      Integer updatedCount = waitingTokenRepository.pushQueue(ids);
      if (updatedCount > 0) {
        log.info(updatedCount + " 개의 대기 토큰이 활성화 되었습니다.");
      }
    } else {
      log.info("대기중인 토큰이 없습니다.");
    }
  }

  //만료시간이 지난 대기열 토큰 상태->만료 처리
  @Transactional
  @Scheduled(fixedRate = 30_000)
  public void expirewaitingToken(){
    //만료시간이 지난 토큰 가져옴
    List<WaitingTokenEntity> expiredwaitingTokens = waitingTokenRepository.getExpiredwaitingTokens(
        LocalDateTime.now());
    //예약취소로 업데이트
    if (!expiredwaitingTokens.isEmpty()) {
      for (WaitingTokenEntity expiredwaitingToken : expiredwaitingTokens) {
        expiredwaitingToken.setStatus(Status.EXPIRED);
      }
      waitingTokenRepository.saveAll(expiredwaitingTokens);
    } else {
      log.info("기간 만료 된 토큰이 없습니다.");
    }
  }
}
