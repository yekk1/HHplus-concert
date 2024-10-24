package com.sparta.hhplusconcert.config;

import com.sparta.hhplusconcert.common.WaitingTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final WaitingTokenInterceptor  waitingTokenInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(waitingTokenInterceptor)
        .addPathPatterns("/v1/concerts/**")
        .excludePathPatterns("/v1/concerts/waiting-token", "/v1/concerts/waiting-position");
  }
}
