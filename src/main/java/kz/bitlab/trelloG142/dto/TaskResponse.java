package kz.bitlab.trelloG142.dto;

import kz.bitlab.trelloG142.enums.TaskStatus;

import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        String creatorUsername,
        String assigneeUsername) {
}
