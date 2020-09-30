package com.mduczmal.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostsRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FetchService fetchService;

    @Test
    public void getPostsStatusOK() throws Exception {
        this.mockMvc.perform(get("/posts")).andExpect(status().isOk());
    }

    @Test
    public void getPostsMediaTypeJSON() throws Exception {
        this.mockMvc.perform(get("/posts")).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchPostsStatusCreated() throws Exception {
        int testId = 1;
        Post testPost = new Post();
        testPost.setId(testId);
        when(fetchService.fetch()).thenReturn(List.of(testPost));
        this.mockMvc.perform(post("/posts")).andExpect(status().isCreated());
    }
}


