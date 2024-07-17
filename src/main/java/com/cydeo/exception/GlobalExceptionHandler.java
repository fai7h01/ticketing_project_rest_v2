package com.cydeo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> userNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionWrapper> userAlreadyExistException(UserAlreadyExistException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }


    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> projectNotFoundException(ProjectNotFoundException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(ProjectAlreadyExistException.class)
    public ResponseEntity<ExceptionWrapper> projectAlreadyExistException(ProjectAlreadyExistException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }

    @ExceptionHandler(TaskAlreadyExistException.class)
    public ResponseEntity<ExceptionWrapper> taskAlreadyExistException(TaskAlreadyExistException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> taskNotFoundException(TaskNotFoundException exception, HttpServletRequest request) {
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionWrapper> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request){

        exception.printStackTrace();

        String message = "Invalid input(s)";

        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.BAD_REQUEST.value(), message, request.getRequestURI());
        List<ValidationException> validationExceptions = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {

            String errorField = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            String reason = error.getDefaultMessage();

            ValidationException validationException = new ValidationException(errorField,rejectedValue,reason);

            validationExceptions.add(validationException);
        }

        exceptionWrapper.setErrorCount(validationExceptions.size());
        exceptionWrapper.setValidationExceptionList(validationExceptions);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionWrapper);
    }
}
