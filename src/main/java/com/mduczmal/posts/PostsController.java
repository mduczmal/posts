package com.mduczmal.posts;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class PostsController {
    private final FetchService fetchService;
    private final PostRepository postRepository;

    public PostsController(FetchService fetchService, PostRepository postRepository) {
        this.fetchService = fetchService;
        this.postRepository = postRepository;
    }

    @GetMapping(value = "/posts",produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> posts() {
        return Collections.singletonMap("first", "post");
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<String> fetchPosts() {
        List<Post> posts = fetchService.fetch();
        if (posts.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        postRepository.saveAll(posts);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
