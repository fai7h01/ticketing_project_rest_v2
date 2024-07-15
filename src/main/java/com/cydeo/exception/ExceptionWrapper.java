package com.cydeo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionWrapper {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;

    public ExceptionWrapper(Integer code, String message, String path){
        this.status = code;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

}
