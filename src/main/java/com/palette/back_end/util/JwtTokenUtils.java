package com.palette.back_end.util;


import com.palette.back_end.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtils {

  public static Long getUserId(String token, String key) {
    return extractClaims(token, key).get("userId", Long.class);
  }

  public static String getEmail(String token, String key) {
    return extractClaims(token, key).get("email", String.class);
  }

  public static boolean isExpired(String token, String key) {
    return extractClaims(token, key).getExpiration().before(new Date());
  }

  private static Claims extractClaims(String token, String key) {
    return Jwts.parser()
            .verifyWith(getKey(key))
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public static String generateToken(User user, String key) {
    Claims claims = Jwts.claims()
            .add("userId", user.getUserId())
            .add("email", user.getEmail())
            .build();

    return Jwts.builder()
            .claims(claims)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(getKey(key))
            .compact();
  }

  private static SecretKey getKey(String key) {
    return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
  }
}

