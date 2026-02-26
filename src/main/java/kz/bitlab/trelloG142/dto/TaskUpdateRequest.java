package kz.bitlab.trelloG142.dto;

import kz.bitlab.trelloG142.enums.TaskStatus;

public record TaskUpdateRequest(
        String title,
        String description,
        TaskStatus status,
        String assigneeUsername) {
}
