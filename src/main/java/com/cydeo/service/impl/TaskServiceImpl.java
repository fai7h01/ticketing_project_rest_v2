package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.exception.TaskAlreadyExistException;
import com.cydeo.exception.TaskNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MapperUtil mapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, MapperUtil mapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.map(task -> mapper.convert(task, new TaskDTO()))
                .orElseThrow(() -> new TaskNotFoundException("Task " + id + "does not exists."));
    }

    @Override
    public void save(TaskDTO dto) {
        if (taskRepository.findById(dto.getId()).isPresent()){
            throw new TaskAlreadyExistException("Task " + dto.getId() + " is already exists.");
        }
        dto.setAssignedDate(LocalDate.now());
        dto.setStatus(Status.OPEN);
        taskRepository.save(mapper.convert(dto, new Task()));
    }

    @Override
    public void delete(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()) {
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> foundTask = taskRepository.findById(dto.getId());
        Task convertedDto = mapper.convert(dto, new Task());
        if (foundTask.isPresent()) {
            convertedDto.setStatus(dto.getStatus() == null ? foundTask.get().getStatus() : dto.getStatus());
            convertedDto.setAssignedDate(foundTask.get().getAssignedDate());
            taskRepository.save(convertedDto);
        }
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO dto) {
        Project project = mapper.convert(dto, new Project());
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO dto) {
        Project project = mapper.convert(dto, new Project());
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.stream().map(task -> mapper.convert(task, new TaskDTO())).forEach(taskDTO -> {
            taskDTO.setStatus(Status.COMPLETE);
            update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllByStatusIsNot(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.
                findAllByStatusIsNotAndAssignedEmployee(status, mapper.convert(loggedInUser, new User()));
        return tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllByStatusIs(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.
                findAllByStatusAndAssignedEmployee(status, mapper.convert(loggedInUser, new User()));
        return tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {
        List<Task> list = taskRepository
                .findAllByStatusIsNotAndAssignedEmployee(Status.COMPLETE, mapper.convert(assignedEmployee, new User()));
        return list.stream().map(task -> mapper.convert(task, new TaskDTO())).collect(Collectors.toList());
    }
}
