package com.pro.shopfee.model;

import com.google.gson.Gson;

public class User {

    private String email;
    private String password;
    private String role;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public String toJSon() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
