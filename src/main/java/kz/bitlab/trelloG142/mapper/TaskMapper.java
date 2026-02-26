package kz.bitlab.trelloG142.mapper;

import kz.bitlab.trelloG142.dto.TaskCreateRequest;
import kz.bitlab.trelloG142.dto.TaskResponse;
import kz.bitlab.trelloG142.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskCreateRequest request);

    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(source = "assignee.username", target = "assigneeUsername")
    TaskResponse toDTO(Task task);

    List<TaskResponse> toDTOList(List<Task> tasks);
}
