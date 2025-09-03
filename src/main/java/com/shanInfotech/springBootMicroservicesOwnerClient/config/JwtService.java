package com.shanInfotech.springBootMicroservicesOwnerClient.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  private final SecretKey key;
  private final long expirationMillis;

  public JwtService(
      @Value("${jwt.secret}") String base64Secret,
      @Value("${jwt.expiration-minutes}") long expirationMinutes) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    this.expirationMillis = expirationMinutes * 60_000L;
  }

  public String generateToken(UserDetails user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(Date.from(now))
        .expiration(new Date(now.toEpochMilli() + expirationMillis))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean isTokenValid(String token, UserDetails user) {
    String sub = extractUsername(token);
    Instant exp = Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration().toInstant();
    return sub.equals(user.getUsername()) && exp.isAfter(Instant.now());
  }
}

