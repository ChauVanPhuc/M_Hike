package com.example.m_hike.Models;

public class Favorites {
    private int favorites_id;
    private int hike_id;
    private int user_id;

    public Favorites(int favorites_id, int hike_id, int user_id) {
        this.favorites_id = favorites_id;
        this.hike_id = hike_id;
        this.user_id = user_id;
    }

    public int getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(int favorites_id) {
        this.favorites_id = favorites_id;
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
}
