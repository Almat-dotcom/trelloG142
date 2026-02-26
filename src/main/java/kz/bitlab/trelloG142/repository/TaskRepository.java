package kz.bitlab.trelloG142.repository;

import kz.bitlab.trelloG142.model.Task;
import kz.bitlab.trelloG142.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByCreator(User user, Pageable pageable);

    Page<Task> findAllByAssigneeUsername(String username, Pageable pageable);
}
