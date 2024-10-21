package com.sparta.hhplusconcert.domain.queue.entity;

import com.sparta.hhplusconcert.domain.common.Status;
import com.sparta.hhplusconcert.domain.common.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QUEUE_TOKEN")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QueueTokenEntity extends TimeBaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false, updatable = false)
  private LocalDateTime issuedTime;

  @Column(nullable = false, updatable = false)
  private LocalDateTime expiredTime;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiredTime) || this.status == Status.EXPIRED;
  }

  public boolean isTokenPassedQueue() {
    if (this.status == Status.CONNECTED) {
      return true;
    } else {
      return false;
    }
  }
}
