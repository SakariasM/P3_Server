package com.p3.Server.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Så spring boot ved at det er mysql auto increment
    private int user_id;
    private String username;
    private String password;
    private String role;


    public Users(){

    }

    public Users(int user_id, String username, String password, String role) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Users(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }       // Hvis databasen ikke incrementer korrekt, men i princippet betyder det ikke noget?
    public int getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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

    @Override       // Override as toJSON?
    public String toString() {
        return "user{" +
                "userID=" + user_id +
                ", username='" + username +
                ", password='" + password +
                ", role='" + role +
                '}';
    }


}
