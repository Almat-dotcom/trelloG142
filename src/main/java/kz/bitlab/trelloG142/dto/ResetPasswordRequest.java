package kz.bitlab.trelloG142.dto;

public record ResetPasswordRequest(
            String resetToken,
            String newPassword
    ) {}