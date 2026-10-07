package com.example.backend.models;

public class User {

    private long id;
    private String name;
    private String mail;
    private String pass;

    public User(long id, String name, String mail, String pass) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.pass = pass;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }
}