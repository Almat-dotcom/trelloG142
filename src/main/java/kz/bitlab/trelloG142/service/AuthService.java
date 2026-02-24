package kz.bitlab.trelloG142.service;

import kz.bitlab.trelloG142.dto.*;

public interface AuthService {
    TokenResponse login(LoginRequest request);

    TokenResponse refresh(RefreshRequest request);

    TokenResponse register(RegisterRequest req);

    void logout(String token);

    String forgotPassword(ForgotPasswordRequest req);

    void resetPassword(ResetPasswordRequest req);
}
