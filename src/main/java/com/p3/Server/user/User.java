package com.p3.Server.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    private String username;
    private String full_name;
    private Boolean clocked_in;
    private Boolean on_break;
    private Boolean logged_in;
    private String password;
    private String role;

    public User() {}

    public User(int user_id, String username, String full_name, Boolean clocked_in, Boolean on_break, Boolean logged_in, String password, String role) {
        this.user_id = user_id;
        this.username = username;
        this.full_name = full_name;
        this.clocked_in = clocked_in;
        this.on_break = on_break;
        this.logged_in = logged_in;
        this.password = password;
        this.role = role;
    }

    @JsonProperty("user_id")
    public int getUserId() {
        return user_id;
    }
    @JsonProperty("user_id")
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("full_name")
    public String getFullName() {
        return full_name;
    }
    @JsonProperty("full_name")
    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    @JsonProperty("clocked_in")
    public Boolean getClockedIn() {
        return clocked_in;
    }
    @JsonProperty("clocked_in")
    public void setClockedIn(Boolean clocked_in) {
        this.clocked_in = clocked_in;
    }

    @JsonProperty("on_break")
    public Boolean getOnBreak() {
        return on_break;
    }
    @JsonProperty("on_break")
    public void setOnBreak(Boolean on_break) {
        this.on_break = on_break;
    }

    @JsonProperty("logged_in")
    public Boolean getLoggedIn() {
        return logged_in;
    }
    @JsonProperty("logged_in")
    public void setLoggedIn(Boolean logged_in) {
        this.logged_in = logged_in;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }
    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", clocked_in=" + clocked_in +
                ", on_break=" + on_break +
                ", logged_in=" + logged_in +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}