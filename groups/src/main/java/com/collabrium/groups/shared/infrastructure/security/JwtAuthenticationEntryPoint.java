package com.collabrium.groups.shared.infrastructure.security;

import com.collabrium.groups.shared.interfaces.rest.exceptions.AuthenticationErrorResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {

    String exceptionMessage = authException.getMessage();

    String code;
    String message;
    List<String> details;

    if (exceptionMessage.contains("Invalid signature")) {

      code = "AUTH-004";

      message = "Invalid JWT signature";

      details = List.of(
          "The JWT signature is invalid",
          "The token may have been tampered with"
      );

    } else if (exceptionMessage.contains("Malformed")) {

      code = "AUTH-002";

      message = "Malformed JWT token";

      details = List.of(
          "The JWT token format is invalid",
          "Expected format: Bearer <token>"
      );

    } else if (exceptionMessage.contains("Expired")) {

      code = "AUTH-003";

      message = "Expired JWT token";

      details = List.of(
          "The JWT token has expired",
          "Authenticate again"
      );

    } else {

      code = "AUTH-001";

      message = "JWT token is missing";

      details = List.of(
          "Authorization header is required",
          "Expected format: Bearer <token>"
      );
    }

    AuthenticationErrorResource error =
        new AuthenticationErrorResource(
            Instant.now().toString(),
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            code,
            message,
            request.getRequestURI(),
            details
        );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    objectMapper.writeValue(response.getOutputStream(), error);
  }
}