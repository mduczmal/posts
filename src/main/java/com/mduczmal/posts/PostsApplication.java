package com.mduczmal.posts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PostsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostsApplication.class, args);
    }

}
