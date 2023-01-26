package com.cydeo.mapper;

import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // if one class itself it is injecting somewhere, or this class has dependency from other class - @Component
public class TaskMapper {

    private final ModelMapper modelMapper; // if put final, you will get warning for constructor, without final you will not

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task convertToEntity(TaskDTO dto){ // from dto to entity
        return modelMapper.map(dto, Task.class);
    }

    public TaskDTO convertToDto(Task entity){ // from entity to dto
        return modelMapper.map(entity, TaskDTO.class);
    }
}
