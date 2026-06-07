package com.coderank.coderank.entity;

import jakarta.persistence.*;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column(columnDefinition = "TEXT")
    private String output;

    private String status;

    // ---------------- GETTERS ----------------

    public Long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }

    public String getOutput() {
        return output;
    }

    public String getStatus() {
        return status;
    }

    // ---------------- SETTERS ----------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}