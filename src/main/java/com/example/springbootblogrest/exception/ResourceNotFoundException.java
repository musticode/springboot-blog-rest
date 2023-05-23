package com.example.springbootblogrest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private long id;

    public ResourceNotFoundException(String resourceName, String fieldName, long id){
        super(String.format("%s not found with %s : '%s", resourceName, fieldName, id));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }


}
