package com.rodiugurlu.apigateway.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(Object::toString)
                        .toList()) // Kullanıcının rollerini token içine ekle
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getKey())
                .compact();
    }


    public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
        Claims claims = getClaims(token);
        return claimsFunction.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expiredDate = exportToken(token, Claims::getExpiration);
        return expiredDate.before(new Date());
    }

    public String getUsernameByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public List<String> getRoles(String token) {
        return exportToken(token, claims -> claims.get("roles", List.class));
    }


    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}