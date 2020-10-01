package com.mduczmal.posts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostNotFoundAdvice {

    @ExceptionHandler(PostNotFoundException.class)
    ResponseEntity<String> postNotFoundHandler() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}