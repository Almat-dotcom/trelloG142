package kz.bitlab.trelloG142.controller;

import kz.bitlab.trelloG142.dto.TaskCreateRequest;
import kz.bitlab.trelloG142.dto.TaskResponse;
import kz.bitlab.trelloG142.dto.TaskUpdateRequest;
import kz.bitlab.trelloG142.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskCreateRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> creatorTasks(Authentication auth,
                                                           @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.creatorTasks(pageable, auth.getName()));
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable UUID id,
                               @RequestBody TaskUpdateRequest req,
                               Authentication auth) {
        return taskService.update(id, req, auth.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, Authentication auth) {
        taskService.delete(id, auth.getName());
    }

    @GetMapping("/my")
    public List<TaskResponse> my(Authentication auth,
                                 @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return taskService.myTasks(auth.getName(), pageable);
    }

}
