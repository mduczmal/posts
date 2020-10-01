package com.mduczmal.posts;

public class PostNotFoundException extends RuntimeException {
    PostNotFoundException(Integer id) {
        super("Post with id " + id + "not found");
    }
}
