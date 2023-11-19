package com.example.m_hike.Models;

public class LikeBlog {
    private int blog_id;
    private int user_id;

    public LikeBlog(int blog_id, int user_id) {
        this.blog_id = blog_id;
        this.user_id = user_id;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
