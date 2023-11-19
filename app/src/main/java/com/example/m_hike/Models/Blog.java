package com.example.m_hike.Models;

public class Blog {
    private int blog_id;
    private int hike_id;
    private int user_id;
    private String blog_caption;
    private String blog_date;

    public Blog(int blog_id, int hike_id, int user_id, String blog_caption, String blog_date) {
        this.blog_id = blog_id;
        this.hike_id = hike_id;
        this.user_id = user_id;
        this.blog_caption = blog_caption;
        this.blog_date = blog_date;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public int getHike_id() {
        return hike_id;
    }

    public void setHike_id(int hike_id) {
        this.hike_id = hike_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBlog_caption() {
        return blog_caption;
    }

    public void setBlog_caption(String blog_caption) {
        this.blog_caption = blog_caption;
    }

    public String getBlog_date() {
        return blog_date;
    }

    public void setBlog_date(String blog_date) {
        this.blog_date = blog_date;
    }
}
