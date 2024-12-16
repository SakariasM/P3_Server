package com.p3.Server.weeklyTimelog;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class WeeklyTimelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Så spring boot ved at det er mysql auto increment
    private int weeklyId;
    private int userId;
    private String full_name;
    private LocalDate weekStart;
    private String totalHoursWorked;


    public WeeklyTimelog() {

    }

    public WeeklyTimelog(int userId, String full_name, LocalDate weekStart, String totalHoursWorked) {
        this.userId = userId;
        this.full_name = full_name;
        this.weekStart = weekStart;
        this.totalHoursWorked = totalHoursWorked;
    }

    public int getWeeklyId() {return weeklyId;}
    public void setWeeklyId(int weeklyId) {this.weeklyId = weeklyId;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public String getFullname() {return full_name;}
    public void setFullname(String full_name) { this.full_name = full_name; }
    public LocalDate getWeekStart() {return weekStart;}
    public void setWeekStart(LocalDate weekStart) {this.weekStart = weekStart;}
    public String getTotalHoursWorked() {return totalHoursWorked;}
    public void setTotalHoursWorked(String totalHoursWorked) {this.totalHoursWorked = totalHoursWorked;}

    @Override       // Override as toJSON?
    public String toString() {
        return "weeklyTimelog{" +
                "weeklyId=" + weeklyId +
                ", userId'" + userId +
                ", full_name'" + full_name +
                ", weekStart='" + weekStart +
                ", totalHoursWorked='" + totalHoursWorked +
                '}';
    }
}