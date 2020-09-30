package com.mduczmal.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostsRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FetchService fetchService;

    @MockBean
    private UpdateService updateService;

    @MockBean
    private PostRepository postRepository;

    @Test
    public void getPostsStatusOK() throws Exception {
        this.mockMvc.perform(get("/posts")).andExpect(status().isOk());
    }

    @Test
    public void getPostsMediaTypeHAL_JSON() throws Exception {
        this.mockMvc.perform(get("/posts")).andExpect(content().contentType(HAL_JSON));
    }

    @Test
    public void getPostsContent() throws Exception {
        Post testPost1 = new Post();
        testPost1.setId(1);
        testPost1.setUserId(11);
        testPost1.setTitle("testTitle1");
        testPost1.setBody("testBody1");
        Post testPost2 = new Post();
        testPost2.setId(2);
        testPost2.setUserId(12);
        testPost2.setTitle("testTitle2");
        testPost2.setBody("testBody2");

        when(postRepository.findAll()).thenReturn(List.of(testPost1, testPost2));
        this.mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$._embedded.posts", hasSize(2)))
                .andExpect(jsonPath("$._embedded.posts[0]", aMapWithSize(4)))
                .andExpect(jsonPath("$._embedded.posts[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.posts[0].title", is("testTitle1")))
                .andExpect(jsonPath("$._embedded.posts[0].body", is("testBody1")))
                .andExpect(jsonPath("$._embedded.posts[0]._links", aMapWithSize(2)))
                .andExpect(jsonPath("$._embedded.posts[1]", aMapWithSize(4)))
                .andExpect(jsonPath("$._embedded.posts[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.posts[1].title", is("testTitle2")))
                .andExpect(jsonPath("$._embedded.posts[1].body", is("testBody2")))
                .andExpect(jsonPath("$._embedded.posts[1]._links", aMapWithSize(2)));
    }

    @Test
    public void fetchPosts() throws Exception {
        int testId = 1;
        int testUserId = 1;
        Post testPost = new Post();
        testPost.setId(testId);
        testPost.setUserId(testUserId);
        testPost.setTitle("testTitle");
        testPost.setBody("testBody");
        when(fetchService.fetch()).thenReturn(List.of(testPost));

        this.mockMvc.perform(post("/posts")).andExpect(status().isCreated());
        verify(fetchService).fetch();
        verify(updateService).updatePosts(anyList());
    }

    @Test
    public void deletePost() throws Exception {
        Post testPost1 = new Post();
        testPost1.setId(1);
        testPost1.setUserId(11);
        testPost1.setTitle("testTitle1");
        testPost1.setBody("testBody1");
        Post testPost2 = new Post();
        testPost2.setId(2);
        testPost2.setUserId(12);
        testPost2.setTitle("testTitle2");
        testPost2.setBody("testBody2");

        when(postRepository.findById(1)).thenReturn(Optional.of(testPost1));
        when(postRepository.findAll()).thenReturn(List.of(testPost1, testPost2));
        this.mockMvc.perform(delete("/posts/1")).andExpect(status().isNoContent());
        this.mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$._embedded.posts", hasSize(1)))
                .andExpect(jsonPath("$._embedded.posts[0]", aMapWithSize(4)))
                .andExpect(jsonPath("$._embedded.posts[0].id", is(2)))
                .andExpect(jsonPath("$._embedded.posts[0].title", is("testTitle2")))
                .andExpect(jsonPath("$._embedded.posts[0].body", is("testBody2")))
                .andExpect(jsonPath("$._embedded.posts[0]._links", aMapWithSize(2)));
    }
    @Test
    public void modifyTitle() throws Exception {
        Post testPost1 = new Post();
        testPost1.setId(1);
        testPost1.setUserId(11);
        testPost1.setTitle("testTitle1");
        testPost1.setBody("testBody1");
        Post testPost2 = new Post();
        testPost2.setId(2);
        testPost2.setUserId(12);
        testPost2.setTitle("testTitle2");
        testPost2.setBody("testBody2");

        when(postRepository.findById(2)).thenReturn(Optional.of(testPost2));
        when(postRepository.findAll()).thenReturn(List.of(testPost1, testPost2));
        this.mockMvc.perform(put("/posts/2?title=modifiedTitle2")).andExpect(status().isNoContent());
        this.mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$._embedded.posts", hasSize(2)))
                .andExpect(jsonPath("$._embedded.posts[1].title", is("modifiedTitle2")));
        assertTrue(testPost2.isModified());
        assertFalse(testPost1.isModified());
        verify(postRepository).save(any(Post.class));

    }
    @Test
    public void modifyBody() throws Exception {
        Post testPost1 = new Post();
        testPost1.setId(1);
        testPost1.setUserId(11);
        testPost1.setTitle("testTitle1");
        testPost1.setBody("testBody1");
        Post testPost2 = new Post();
        testPost2.setId(2);
        testPost2.setUserId(12);
        testPost2.setTitle("testTitle2");
        testPost2.setBody("testBody2");

        when(postRepository.findById(1)).thenReturn(Optional.of(testPost1));
        when(postRepository.findAll()).thenReturn(List.of(testPost1, testPost2));
        this.mockMvc.perform(put("/posts/1?body=modifiedBody1")).andExpect(status().isNoContent());
        this.mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$._embedded.posts", hasSize(2)))
                .andExpect(jsonPath("$._embedded.posts[0].body", is("modifiedBody1")));
        assertTrue(testPost1.isModified());
        assertFalse(testPost2.isModified());
        verify(postRepository).save(any(Post.class));

    }
}


