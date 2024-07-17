package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/v2/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Admin","Manager"})
    public ResponseEntity<ResponseWrapper> findAllUsers(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User List is successfully retrieved.")
                .data(userService.listAllUsers()).build());
    }

    @GetMapping("/{username}")
    @RolesAllowed({"Admin","Manager"})
    public ResponseEntity<ResponseWrapper> findByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User " + username + " is successfully retrieved.")
                .data(userService.findByUserName(username)).build());
    }

    @PostMapping
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody @Valid UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("User is successfully created.").build());
    }

    @PutMapping
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody @Valid UserDTO userDTO){
        userService.update(userDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User is successfully updated.").build());
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username){
        userService.delete(username);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User " + username + " is successfully deleted.").build());
    }
}
