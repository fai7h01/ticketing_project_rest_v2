package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/v2/user")
@Tag(description = "User Controller", name = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExecutionTime
    @GetMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Get All Users")
    public ResponseEntity<ResponseWrapper> findAllUsers(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User List is successfully retrieved.")
                .data(userService.listAllUsers()).build());
    }

    @ExecutionTime
    @GetMapping("/{username}")
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Get User By Username")
    public ResponseEntity<ResponseWrapper> findByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User " + username + " is successfully retrieved.")
                .data(userService.findByUserName(username)).build());
    }

    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create User")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody @Valid UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("User is successfully created.").build());
    }

    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody @Valid UserDTO userDTO){
        userService.update(userDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User is successfully updated.").build());
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete User")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username){
        userService.delete(username);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User " + username + " is successfully deleted.").build());
    }
}
