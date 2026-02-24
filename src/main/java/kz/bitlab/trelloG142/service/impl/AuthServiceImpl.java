package kz.bitlab.trelloG142.service.impl;

import kz.bitlab.trelloG142.dto.*;
import kz.bitlab.trelloG142.model.PasswordResetToken;
import kz.bitlab.trelloG142.model.RefreshToken;
import kz.bitlab.trelloG142.model.Role;
import kz.bitlab.trelloG142.model.User;
import kz.bitlab.trelloG142.repository.PasswordResetTokenRepository;
import kz.bitlab.trelloG142.repository.RefreshTokenRepository;
import kz.bitlab.trelloG142.repository.RoleRepository;
import kz.bitlab.trelloG142.repository.UserRepository;
import kz.bitlab.trelloG142.security.TokenHash;
import kz.bitlab.trelloG142.service.AuthService;
import kz.bitlab.trelloG142.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${app.jwt.refresh-token-hours}")
    private long refreshTokenHours;

    @Value("${app.jwt.reset-token-minutes}")
    private long resetTokenMinutes;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new RuntimeException("User not found"));
        String access = jwtService.generateAccessToken(user);
        String refresh = issueRefreshToken(user);

        return new TokenResponse(access, refresh);
    }

    @Override
    public TokenResponse refresh(RefreshRequest request) {
        if (request.refreshToken() == null)
            throw new RuntimeException("refreshToken required");

        String hash = TokenHash.sha256(request.refreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (token.isRevoked() || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired/revoked");
        }

        User user = token.getUser();
        String newAccess = jwtService.generateAccessToken(user);

        return new TokenResponse(newAccess, request.refreshToken());
    }

    @Override
    public TokenResponse register(RegisterRequest req) {
        if (req.username() == null || req.password() == null) {
            throw new RuntimeException("username/password required");
        }

        userRepository.findByUsername(req.username()).ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User u = new User();
        u.setUsername(req.username());
        u.setName(req.name());
        u.setSurname(req.surname());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setRoles(List.of(userRole));

        User saved = userRepository.save(u);
        String access = jwtService.generateAccessToken(saved);
        String refresh = issueRefreshToken(saved);

        return new TokenResponse(access, refresh);
    }

    @Override
    public void logout(String refreshToken) {
        if (refreshToken == null) return;
        String hash = TokenHash.sha256(refreshToken);
        refreshTokenRepository.findByTokenHashAndRevokedFalse(hash).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest req) {
        User user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String rawReset = randomToken();
        String hash = TokenHash.sha256(rawReset);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setTokenHash(hash);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(resetTokenMinutes));
        prt.setUsed(false);

        passwordResetTokenRepository.save(prt);

        return rawReset;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        String hash = TokenHash.sha256(req.resetToken());

        PasswordResetToken token = passwordResetTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired/used");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);

        token.setUsed(true);
        passwordResetTokenRepository.save(token);

        refreshTokenRepository.deleteAllByUser(user);
    }


    //TODO index база данных
    private String issueRefreshToken(User user) {
        String raw = randomToken();
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            hash = HexFormat.of().formatHex(dig);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash);
        refreshToken.setExpiresAt(LocalDateTime.now().plusHours(refreshTokenHours));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return raw;
    }

    private String randomToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
