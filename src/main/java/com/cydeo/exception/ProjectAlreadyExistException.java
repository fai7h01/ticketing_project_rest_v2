package com.cydeo.exception;

public class ProjectAlreadyExistException extends RuntimeException{

    public ProjectAlreadyExistException(String message){
        super(message);
    }
}
