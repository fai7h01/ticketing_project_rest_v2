package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
