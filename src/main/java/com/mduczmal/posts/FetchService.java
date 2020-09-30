package com.mduczmal.posts;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FetchService {
    private final RestTemplate restTemplate;
    private final String postsURL = "https://jsonplaceholder.typicode.com/posts";

    public FetchService(PostRepository postRepository) {
        this.restTemplate = new RestTemplate();
    }
    public List<Post> fetch() {
        ResponseEntity<Post[]> response = restTemplate.getForEntity(postsURL, Post[].class);
        Post[] rawPosts = response.getBody();
        if (rawPosts == null) return List.of();
        return Arrays.asList(rawPosts);
    }
}
