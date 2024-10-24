package com.sparta.hhplusconcert.exception;

import com.sparta.hhplusconcert.concert.domain.InvalidTokenException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Tag(name = "에러 테스트", description = "에러핸들러 관련 테스트입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/errorTest")
public class TestController {
    @PostMapping("/runtime")
    public ResponseEntity<Object> getRuntimeError() {
      throw new RuntimeException("runtime error test");
    }

  @GetMapping("/token-invalid")
  public ResponseEntity<Object> getInvalidTokenError() {
    throw new InvalidTokenException(TokenErrorCode.INVALID_TOKEN);
  }

  @GetMapping("/token-expire")
  public ResponseEntity<Object> getExpiredTokenError() {
    throw new InvalidTokenException(TokenErrorCode.EXPIRED_WAITING_TOKEN);
  }
}
