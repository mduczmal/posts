package com.mduczmal.posts;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PostsController {
    private final FetchService fetchService;
    private final PostRepository postRepository;

    public PostsController(FetchService fetchService, PostRepository postRepository) {
        this.fetchService = fetchService;
        this.postRepository = postRepository;
    }

    @GetMapping(value = "/posts",produces=MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Info>> posts() {
        List<EntityModel<Info>> infos = postRepository.findAll().stream()
                .filter(post -> !post.deleted)
                .map(Post::getInfo)
                .map(info -> EntityModel.of(info,
                        linkTo(methodOn(PostsController.class).posts()).withRel("posts")))
                .collect(Collectors.toList());

        return CollectionModel.of(infos, linkTo(methodOn(PostsController.class).posts()).withSelfRel());
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<String> fetchPosts() {
        List<Post> posts = fetchService.fetch();
        if (posts.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        postRepository.saveAll(posts);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
