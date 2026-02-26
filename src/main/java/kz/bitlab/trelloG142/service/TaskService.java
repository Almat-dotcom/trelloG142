package kz.bitlab.trelloG142.service;

import kz.bitlab.trelloG142.dto.TaskCreateRequest;
import kz.bitlab.trelloG142.dto.TaskResponse;
import kz.bitlab.trelloG142.dto.TaskUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponse create(TaskCreateRequest request, String username);

    List<TaskResponse> creatorTasks(Pageable pageable, String username);

    TaskResponse update(UUID id, TaskUpdateRequest req, String username);

    void delete(UUID id, String username);

    List<TaskResponse> myTasks(String username, Pageable pageable);
}
