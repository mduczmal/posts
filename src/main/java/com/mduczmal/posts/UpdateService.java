package com.mduczmal.posts;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateService {
    private final FetchService fetchService;
    private final PostRepository postRepository;
    private final int MILLISECONDS_IN_DAY = 86400000;
    private final int INIT_DELAY = 5000;

    UpdateService(FetchService fetchService, PostRepository postRepository) {
        this.fetchService = fetchService;
        this.postRepository = postRepository;
    }

    public synchronized void updatePosts(List<Post> fetchedPosts) {
        for (Post fetchedPost : fetchedPosts) {
            postRepository.findById(fetchedPost.getId()).ifPresentOrElse(p -> {
                if(!p.isDeleted() && !p.isModified()) {
                    postRepository.save(fetchedPost);
                }
            },
            () -> postRepository.save(fetchedPost));
        }
    }

    @Scheduled(initialDelay = INIT_DELAY, fixedRate = MILLISECONDS_IN_DAY)
    public void updatePosts() {
        updatePosts(fetchService.fetch());
    }
}