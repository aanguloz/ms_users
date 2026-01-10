package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Puedes mover esta clave a application.yml (¡y nunca exponerla!)
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiry.default:900000}") // 15 min = 900000 ms
    private long defaultAccessTokenExpiryMs;

    @Value("${jwt.refresh-token.expiry.default:3600000}") // 60 min = 3600000 ms
    private long defaultRefreshTokenExpiryMs;

    // --- Métodos principales: generar tokens ---

    public String generateAccessToken(User user, int expiryMinutes) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        // Si tienes roles, agrega: claims.put("roles", user.getRoles());
        return buildToken(claims, user.getUsername(), expiryMinutes * 60_000L);
    }

    public String generateRefreshToken(User user) {
        // El refresh token puede ser más simple (solo identificador)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("type", "refresh");
        return buildToken(claims, user.getUsername(), defaultRefreshTokenExpiryMs);
    }

    // --- Token genérico ---
    private String buildToken(Map<String, Object> extraClaims, String subject, long expiryMs) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Validación y extracción ---

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername)) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // --- Clave segura ---

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
