package com.collabrium.groups.shared.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class JwtAudienceValidator implements OAuth2TokenValidator<Jwt> {

  private final String audience;

  public JwtAudienceValidator(String audience) {
    this.audience = audience;
  }

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {

    List<String> audiences = jwt.getAudience();

    if (audiences.contains(audience)) {
      return OAuth2TokenValidatorResult.success();
    }

    OAuth2Error error = new OAuth2Error(
        "invalid_token",
        "The required audience is missing",
        null
    );

    return OAuth2TokenValidatorResult.failure(error);
  }
}
