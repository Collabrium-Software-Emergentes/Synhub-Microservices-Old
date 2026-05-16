package com.collabrium.api_gateway.infrastructure.security.jwt;

import com.collabrium.api_gateway.infrastructure.config.AuthorizationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@EnableConfigurationProperties(AuthorizationProperties.class)
public class JwtAuthFilter implements WebFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

  private static final String BEARER_PREFIX = "Bearer ";

  private static final String HEADER_USER_ID = "X-User-Id";
  private static final String HEADER_USERNAME = "X-Username";
  private static final String HEADER_ROLES = "X-Roles";

  private final AuthorizationProperties properties;
  private final JwtValidatorService jwtValidatorService;

  public JwtAuthFilter(
      JwtValidatorService jwtValidatorService,
      AuthorizationProperties properties
  ) {

    this.jwtValidatorService = jwtValidatorService;
    this.properties = properties;
  }

  @Override
  @NonNull
  public Mono<Void> filter(
      ServerWebExchange exchange,
      @NonNull WebFilterChain chain
  ) {

    String path = exchange.getRequest().getPath().value();

    if (isPublicPath(path)) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
      return unauthorized(exchange);
    }

    log.info("PATH: {}", path);
    log.info("AUTH HEADER: {}", authHeader);

    try {

      String token = authHeader.substring(BEARER_PREFIX.length());

      if (!jwtValidatorService.validateToken(token)) {
        return unauthorized(exchange);
      }

      ServerWebExchange mutatedExchange = mutateRequest(exchange, token);

      return chain.filter(mutatedExchange);

    } catch (Exception ex) {
      return unauthorized(exchange);
    }
  }

  private boolean isPublicPath(String path) {
    return properties.getPublicPaths().stream()
        .anyMatch(path::startsWith);
  }

  private Mono<Void> unauthorized(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  private ServerWebExchange mutateRequest(ServerWebExchange exchange, String token) {

    String username = jwtValidatorService.getUsername(token);
    Long userId = jwtValidatorService.getUserId(token);
    List<String> roles = jwtValidatorService.getRoles(token);

    ServerHttpRequest mutatedRequest = exchange.getRequest()
        .mutate()
        .header(HEADER_USER_ID, userId != null ? userId.toString() : "")
        .header(HEADER_USERNAME, username != null ? username : "")
        .header(HEADER_ROLES, String.join(",", roles))
        .build();

    return exchange.mutate()
        .request(mutatedRequest)
        .build();
  }
}
