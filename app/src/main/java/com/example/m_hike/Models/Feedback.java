package com.example.m_hike.Models;

public class Feedback {
    private int feedback_id;
    private int user_id;
    private int hike_id;
    private double fb_poin;
    private String fb_des;

    public Feedback(int feedback_id, int user_id, int hike_id, double fb_poin, String fb_des) {
        this.feedback_id = feedback_id;
        this.user_id = user_id;
        this.hike_id = hike_id;
        this.fb_poin = fb_poin;
        this.fb_des = fb_des;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getHike_id() {
        return hike_id;
    }

    public void setHike_id(int hike_id) {
        this.hike_id = hike_id;
    }

    public double getFb_poin() {
        return fb_poin;
    }

    public void setFb_poin(double fb_poin) {
        this.fb_poin = fb_poin;
    }

    public String getFb_des() {
        return fb_des;
    }

    public void setFb_des(String fb_des) {
        this.fb_des = fb_des;
    }
}
