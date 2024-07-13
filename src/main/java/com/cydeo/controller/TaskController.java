package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> findAllTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task list is successfully retrieved.")
                .data(taskService.listAllTasks()).build());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> findById(@PathVariable("taskId") Long id){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully retrieved.")
                .data(taskService.findById(id)).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("Task is successfully created.").build());
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully updated.").build());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskId") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Task is successfully deleted.").build());
    }

    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> findEmployeePendingTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee pending task list is successfully retrieved.")
                .data(taskService.listAllByStatusIsNot(Status.COMPLETE)).build());
    }

    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> findEmployeeArchiveTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee archive task list is successfully retrieved.")
                .data(taskService.listAllByStatusIs(Status.COMPLETE)).build());
    }

    @PutMapping("/employee/update")
    public ResponseEntity<ResponseWrapper> employeeUpdate(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Employee task is successfully updated.").build());
    }


}
