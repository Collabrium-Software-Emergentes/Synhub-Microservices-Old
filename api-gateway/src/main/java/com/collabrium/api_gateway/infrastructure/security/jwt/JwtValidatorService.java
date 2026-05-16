package com.collabrium.api_gateway.infrastructure.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class JwtValidatorService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtValidatorService.class);

  @Value("${authorization.jwt.secret}")
  private String secret;

  @Value("${authorization.jwt.issuer}")
  private String issuer;

  @Value("${authorization.jwt.audience}")
  private String audience;

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .requireIssuer(issuer)
          .requireAudience(audience)
          .build()
          .parseSignedClaims(token);

      LOGGER.debug("Token is valid");
      return true;
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JSON Web Token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public Map<String, Object> getClaims(String token) {
    return Jwts.parser()
        .requireIssuer(issuer)
        .requireAudience(audience)
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getUsername(String token) {
    return (String) getClaims(token).get("sub");
  }

  public Long getUserId(String token) {
    Object userId = getClaims(token).get("user_id");
    if (userId instanceof Integer) {
      return ((Integer) userId).longValue();
    } else if (userId instanceof Long) {
      return (Long) userId;
    }
    return null;
  }

  public List<String> getRoles(String token) {
    Object rolesObj = getClaims(token).get("roles");
    if (rolesObj instanceof List<?>) {
      return ((List<?>) rolesObj).stream()
          .map(Object::toString)
          .toList();
    }
    return Collections.emptyList();
  }
}
