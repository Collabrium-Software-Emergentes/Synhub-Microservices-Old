package com.collabrium.iam.shared.infrastructure.configuration.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GlobalFeignConfiguration {

  private final HttpServletRequest request;

  @Bean
  public RequestInterceptor jwtPropagationInterceptor() {

    return requestTemplate -> {

      String authorizationHeader =
        request.getHeader("Authorization");

      if (authorizationHeader != null) {

        requestTemplate.header(
          "Authorization",
          authorizationHeader
        );
      }
    };
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new FeignErrorDecoder();
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.BASIC;
  }
}