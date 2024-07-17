package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/task")
@Tag(description = "Task Controller", name = "Task API")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get All Tasks")
    public ResponseEntity<ResponseWrapper> findAllTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task list is successfully retrieved.")
                .data(taskService.listAllTasks()).build());
    }

    @GetMapping("/{taskId}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get Task By Id")
    public ResponseEntity<ResponseWrapper> findTaskById(@PathVariable("taskId") Long id){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully retrieved.")
                .data(taskService.findById(id)).build());
    }

    @PostMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Create Task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody @Valid TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("Task is successfully created.").build());
    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update Task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody @Valid TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully updated.").build());
    }

    @DeleteMapping("/{taskId}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete Task")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskId") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully deleted.").build());
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed("Employee")
    @Operation(summary = "Get Employee Pending Tasks")
    public ResponseEntity<ResponseWrapper> findEmployeePendingTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee pending task list is successfully retrieved.")
                .data(taskService.listAllByStatusIsNot(Status.COMPLETE)).build());
    }

    @GetMapping("/employee/archive")
    @RolesAllowed("Employee")
    @Operation(summary = "Get Employee Archive Tasks")
    public ResponseEntity<ResponseWrapper> findEmployeeArchiveTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee archive task list is successfully retrieved.")
                .data(taskService.listAllByStatusIs(Status.COMPLETE)).build());
    }

    @PutMapping("/employee/update")
    @RolesAllowed("Employee")
    @Operation(summary = "Update Task")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee task is successfully updated.").build());
    }


}
