package com.shanInfotech.springBootMicroservicesOwnerClient.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

  private final SecretKey key;
  private final long expirationMillis;

  public JwtService(
      @Value("${jwt.secret:}") String base64Secret,
      @Value("${jwt.expiration-minutes:60}") long expirationMinutes) {

    if (base64Secret == null || base64Secret.isBlank()) {
      throw new IllegalStateException(
          "Missing jwt.secret – define it in application.yml (or tests). Use a 32+ byte base64 key.");
    }
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    this.expirationMillis = Math.max(1, expirationMinutes) * 60_000L;
  }

  /** Generate a token with username (sub) and optional roles claim. */
  public String generateToken(UserDetails user) {
    Instant now = Instant.now();
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder()
        .subject(user.getUsername())
        .claim("roles", roles) // optional but handy
        .issuedAt(Date.from(now))
        .expiration(new Date(now.toEpochMilli() + expirationMillis))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return claims(token).getSubject();
  }

  public boolean isTokenValid(String token, UserDetails user) {
    Claims c = claims(token);
    return user.getUsername().equals(c.getSubject())
        && c.getExpiration().toInstant().isAfter(Instant.now());
  }

  private Claims claims(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
