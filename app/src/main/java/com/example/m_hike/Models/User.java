package com.example.m_hike.Models;

public class User {

    private int user_id;
    private String user_name;
    private String user_birth;
    private  String user_gender;
    private String user_mail;
    private String user_phone;
    private String user_address;
    private String user_job;
    private String user_password;

    public User(int user_id, String user_name, String user_birth, String user_gender, String user_mail,
                String user_phone, String user_address, String user_job, String user_password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_birth = user_birth;
        this.user_gender = user_gender;
        this.user_mail = user_mail;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.user_job = user_job;
        this.user_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_job() {
        return user_job;
    }

    public void setUser_job(String user_job) {
        this.user_job = user_job;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
