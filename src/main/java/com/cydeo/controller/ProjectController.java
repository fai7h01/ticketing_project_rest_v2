package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/project")
@Tag(description = "Project Controller", name = "Project API")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @ExecutionTime
    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get All Projects")
    public ResponseEntity<ResponseWrapper> findAllProjects() {
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project list successfully retrieved.")
                .data(projectService.listAllProjects()).build());
    }

    @ExecutionTime
    @GetMapping("/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get Project By Project Code")
    public ResponseEntity<ResponseWrapper> findByProjectCode(@PathVariable("projectCode") String code) {
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project " + code + " is successfully retrieved.")
                .data(projectService.getByProjectCode(code)).build());
    }

    @PostMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Create Project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("Project is successfully created.").build());
    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update Project")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody @Valid ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project is successfully updated.").build());
    }

    @DeleteMapping("/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete Project")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String code){
        projectService.delete(code);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project " + code + " is successfully deleted.").build());
    }

    @ExecutionTime
    @GetMapping("/manager/project-status")
    @RolesAllowed("Manager")
    @Operation(summary = "Get All Projects By Logged In Manager")
    public ResponseEntity<ResponseWrapper> findManagerProjects() {
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project list is successfully retrieved.")
                .data(projectService.listAllProjectDetails()).build());
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Complete Project")
    public ResponseEntity<ResponseWrapper> completeManagerProject(@PathVariable("projectCode") String code){
        projectService.complete(code);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project is successfully completed.").build());
    }

}
