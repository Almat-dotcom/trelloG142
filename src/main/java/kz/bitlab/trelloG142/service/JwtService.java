package kz.bitlab.trelloG142.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails user);

    String extractUsername(String token);

    boolean isValid(String token, UserDetails user);
}
