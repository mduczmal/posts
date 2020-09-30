package com.mduczmal.posts;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PostsController {
    private final FetchService fetchService;
    private final UpdateService updateService;
    private final PostRepository postRepository;

    public PostsController(FetchService fetchService, UpdateService updateService, PostRepository postRepository) {
        this.fetchService = fetchService;
        this.updateService = updateService;
        this.postRepository = postRepository;
    }

    @GetMapping(value = "/posts/{id}")
    EntityModel<Info> post(@PathVariable Integer id) {
        Info info = postRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new).getInfo();

        return EntityModel.of(info,
                linkTo(methodOn(PostsController.class).post(id)).withSelfRel(),
                linkTo(methodOn(PostsController.class).posts(Optional.empty())).withRel("posts"));
    }

    private boolean match(Info info, Optional<String> titleSearch) {
        String t = titleSearch.orElse(null);
        if (t == null) {
            return true;
        } else {
            return info.getTitle().contains(t);
        }
    }

    @GetMapping(value = "/posts")
    public CollectionModel<EntityModel<Info>> posts(@RequestParam Optional<String> search) {
        List<EntityModel<Info>> infos = postRepository.findAll().stream()
                .filter(post -> !post.isDeleted())
                .map(Post::getInfo)
                .filter(info -> match(info, search))
                .map(info -> EntityModel.of(info,
                        linkTo(methodOn(PostsController.class).post(info.getId())).withSelfRel(),
                        linkTo(methodOn(PostsController.class).posts(Optional.empty())).withRel("posts")))
                .collect(Collectors.toList());

        return CollectionModel.of(infos, linkTo(methodOn(PostsController.class).posts(Optional.empty())).withSelfRel());
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<String> fetchPosts() {
        List<Post> posts = fetchService.fetch();
        updateService.updatePosts(posts);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(PostsController.class).slash("posts").toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(p -> {
            p.setDeleted(true);
            postRepository.save(p);
        });
        return post.isPresent() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "posts/{id}")
    public ResponseEntity<String> modifyPost(@PathVariable Integer id, @RequestParam Optional<String> title,
                                             @RequestParam Optional<String> body) {
        if (title.isEmpty() && body.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(p ->
                {
                    title.ifPresent(p::setTitle);
                    body.ifPresent(p::setBody);
                    p.setModified(true);
                    postRepository.save(p);
                }
        );
        return post.isPresent() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
