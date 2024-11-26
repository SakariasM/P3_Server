package com.p3.Server.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Så spring boot ved at det er mysql auto increment
    private int user_id;
    private String username;
    private String full_name;
    private Boolean clocked_in; // Bool?
    private Boolean on_break;
    private Boolean logged_in;
    private String password;
    private String role;


    public User(){

    }


    public User(int user_id, String username, String full_name, Boolean clocked_in, Boolean on_break, Boolean logged_in, String password, String role) {
        this.user_id = user_id;
        this.username = username;
        this.full_name = full_name;
        this.password = password;
        this.role = role;
        this.clocked_in = clocked_in;
        this.on_break = on_break;
        this.logged_in = logged_in;
    }

    public User(String username, String full_name, Boolean clocked_in, Boolean on_break, Boolean logged_in, String password, String role) {
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.role = role;
        this.clocked_in = clocked_in;
        this.on_break = on_break;
        this.logged_in = logged_in;
    }


    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {this.user_id = user_id;}
    public String getFull_name() {return full_name;}
    public void setFull_name(String full_name) {this.full_name = full_name;}
    public Boolean getClocked_in() {return clocked_in;}
    public void setClocked_in(Boolean clocked_in) {this.clocked_in = clocked_in;}
    public Boolean getOn_break() {return on_break;}
    public void setOn_break(Boolean on_break) {this.on_break = on_break;}
    public Boolean getLogged_in() {return logged_in;}
    public void setLogged_in(Boolean logged_in) {this.logged_in = logged_in;}
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
                ", full_name='" + full_name +
                ", clocked_in=" + clocked_in +
                ", on_break=" + on_break +
                ", logged_in=" + logged_in +
                ", password='" + password +
                ", role='" + role +
                '}';
    }


}
