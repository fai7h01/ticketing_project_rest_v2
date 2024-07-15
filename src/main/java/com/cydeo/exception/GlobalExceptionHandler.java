package com.cydeo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> userNotFoundException(UserNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }


    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionWrapper> userAlreadyExistException(UserAlreadyExistException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }


    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> projectNotFoundException(ProjectNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }
}
