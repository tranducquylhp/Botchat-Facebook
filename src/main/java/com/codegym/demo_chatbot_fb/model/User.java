package com.codegym.demo_chatbot_fb.model;

import javax.persistence.*;

@Entity
@Table(name = "userFacebook")
public class User {
    @Id
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
