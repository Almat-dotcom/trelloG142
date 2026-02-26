package kz.bitlab.trelloG142.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kz.bitlab.trelloG142.model.Role;
import kz.bitlab.trelloG142.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {
    private final SecretKey key;
    private final long accessTokenMinute;

    public JwtServiceImpl(@Value("${app.jwt.secret}") String base64Secret, @Value("${app.jwt.access-token-minutes}") long accessTokenMinutes) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        this.accessTokenMinute = accessTokenMinutes;
    }

    @Override
    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        Instant expired = now.plusSeconds(accessTokenMinute*3600);

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expired))
                .claim("roles", roles)
                .claim("anton", "anton")
                .signWith(key, Jwts.SIG.HS256)
                .compact();

    }

    @Override
    public String extractUsername(String token) {
        return pareClaims(token).getSubject();
    }

    @Override
    public boolean isValid(String token, UserDetails user) {
        Claims claims=pareClaims(token);
        return claims.getSubject().equals(user.getUsername()) && claims.getExpiration().after(new Date());
    }

    private Claims pareClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
