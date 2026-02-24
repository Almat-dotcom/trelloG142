package kz.bitlab.trelloG142.dto;

public record RegisterRequest(
            String username,
            String name,
            String surname,
            String password
    ) {}