package com.collabrium.api_gateway.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationProperties {

  private Jwt jwt;
  private List<String> publicPaths;

  public Jwt getJwt() {
    return jwt;
  }

  public void setJwt(Jwt jwt) {
    this.jwt = jwt;
  }

  public List<String> getPublicPaths() {
    return publicPaths;
  }

  public void setPublicPaths(List<String> publicPaths) {
    this.publicPaths = publicPaths;
  }

  public static class Jwt {
    private String secret;
    private String issuer;
    private String audience;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
  }
}
