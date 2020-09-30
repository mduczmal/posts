package com.mduczmal.posts;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "posts")
public class Info {

    private Integer userId;
    private String title;
    private String body;

    public Info(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
