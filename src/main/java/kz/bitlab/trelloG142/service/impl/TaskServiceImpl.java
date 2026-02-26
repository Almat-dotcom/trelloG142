package kz.bitlab.trelloG142.service.impl;

import kz.bitlab.trelloG142.dto.TaskCreateRequest;
import kz.bitlab.trelloG142.dto.TaskResponse;
import kz.bitlab.trelloG142.dto.TaskUpdateRequest;
import kz.bitlab.trelloG142.enums.ExceptionStatus;
import kz.bitlab.trelloG142.enums.TaskStatus;
import kz.bitlab.trelloG142.exception.TrelloException;
import kz.bitlab.trelloG142.mapper.TaskMapper;
import kz.bitlab.trelloG142.model.Task;
import kz.bitlab.trelloG142.model.User;
import kz.bitlab.trelloG142.repository.TaskRepository;
import kz.bitlab.trelloG142.repository.UserRepository;
import kz.bitlab.trelloG142.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static kz.bitlab.trelloG142.enums.ExceptionStatus.TASK_NOT_FOUND;
import static kz.bitlab.trelloG142.enums.ExceptionStatus.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse create(TaskCreateRequest request, String username) {
        User creator = userRepository.findByUsername(username).orElseThrow(() -> new TrelloException("User not found with username " + username, USER_NOT_FOUND));
        User assignee = null;
        if (!request.assigneeUsername().isBlank()) {
            assignee = userRepository.findByUsername(request.assigneeUsername()).orElseThrow(() -> new TrelloException("User not found with username " + username, USER_NOT_FOUND));
        }

        Task task = taskMapper.toEntity(request);
        task.setAssignee(assignee);
        task.setCreator(creator);
        task.setStatus(TaskStatus.TODO);

        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    @Override
    public List<TaskResponse> creatorTasks(Pageable pageable, String username) {
        User creator = userRepository.findByUsername(username).orElseThrow(() -> new TrelloException("User not found with username " + username, ExceptionStatus.USER_NOT_FOUND));
        Page<Task> tasks = taskRepository.findAllByCreator(creator, pageable);
        return taskMapper.toDTOList(tasks.getContent());
    }

    @Override
    public TaskResponse update(UUID id, TaskUpdateRequest req, String username) {
        Task t = taskRepository.findById(id).orElseThrow(() -> new TrelloException("Task not found by id: " + id, ExceptionStatus.TASK_NOT_FOUND));
        if (!t.getCreator().getUsername().equals(username)) {
            throw new TrelloException("You are not creator", ExceptionStatus.CREATOR_NOT_MATCH);
        }

        if (req.title() != null) t.setTitle(req.title());
        if (req.description() != null) t.setDescription(req.description());
        if (req.status() != null) t.setStatus(req.status());

        if (req.assigneeUsername() != null) {
            if (req.assigneeUsername().isBlank()) {
                t.setAssignee(null);
            } else {
                User a = userRepository.findByUsername(req.assigneeUsername())
                        .orElseThrow();
                t.setAssignee(a);
            }
        }

        Task task = taskRepository.save(t);
        return taskMapper.toDTO(task);
    }

    @Override
    public void delete(UUID id, String username) {
        Task t = taskRepository.findById(id).orElseThrow(() -> new TrelloException("Task not found by id: " + id, ExceptionStatus.TASK_NOT_FOUND));
        if (!t.getCreator().getUsername().equals(username)) {
            throw new TrelloException("You are not creator", ExceptionStatus.CREATOR_NOT_MATCH);
        }
        taskRepository.delete(t);
    }

    @Override
    public List<TaskResponse> myTasks(String username, Pageable pageable) {
        Page pageTasks = taskRepository.findAllByAssigneeUsername(username, pageable);
        return taskMapper.toDTOList(pageTasks.getContent());
    }


}
