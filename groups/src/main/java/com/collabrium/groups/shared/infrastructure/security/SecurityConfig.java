package com.collabrium.groups.shared.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
  private String secretKey;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer}")
  private String issuer;

  @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
  private String audience;

  private final JwtAuthenticationEntryPoint authenticationEntryPoint;

  public SecurityConfig(
      JwtAuthenticationEntryPoint authenticationEntryPoint
  ) {

    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint)
        )

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/**"
            ).permitAll()

            .anyRequest().authenticated()
        )

        .oauth2ResourceServer(oauth -> oauth
            .authenticationEntryPoint(authenticationEntryPoint)
            .jwt(Customizer.withDefaults())
        );

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {

    SecretKey key = new SecretKeySpec(
        secretKey.getBytes(),
        "HmacSHA256"
    );

    NimbusJwtDecoder jwtDecoder =
        NimbusJwtDecoder.withSecretKey(key).build();

    OAuth2TokenValidator<Jwt> withIssuer =
        JwtValidators.createDefaultWithIssuer(issuer);

    OAuth2TokenValidator<Jwt> audienceValidator =
        new JwtAudienceValidator(audience);

    OAuth2TokenValidator<Jwt> validator =
        new DelegatingOAuth2TokenValidator<>(
            withIssuer,
            audienceValidator
        );

    jwtDecoder.setJwtValidator(validator);

    return jwtDecoder;
  }
}
