package com.codegym.demo_chatbot_fb.model;

import javax.persistence.*;

@Entity
@Table(name = "code_exercise")
public class CodeExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;
    private boolean status = true;

    public CodeExercise() {
    }

    public CodeExercise(Long id, String title, String content, boolean status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public CodeExercise(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
