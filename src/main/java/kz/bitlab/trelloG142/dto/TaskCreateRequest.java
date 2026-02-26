package kz.bitlab.trelloG142.dto;

public record TaskCreateRequest(
        String title,
        String description,
        String assigneeUsername) {
}
