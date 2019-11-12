package com.codegym.demo_chatbot_fb.model;

public class User {
    private String id;

    private boolean status = false;

    public User() {
    }

    public User(String id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
