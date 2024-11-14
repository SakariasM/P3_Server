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
    private LocalDate weekStart;
    private LocalTime totalHoursWorked;     // I databasen bruger vi bare time til totalhoursworked, så skal vi også bruge det til timelog event_time?


    public WeeklyTimelog() {

    }

    public WeeklyTimelog(int weeklyId, int userId, LocalDate weekStart, LocalTime totalHoursWorked) {
        this.weeklyId = weeklyId;
        this.userId = userId;
        this.weekStart = weekStart;
        this.totalHoursWorked = totalHoursWorked;
    }

    public WeeklyTimelog(int userId, LocalDate weekStart, LocalTime totalHoursWorked) {
        this.userId = userId;
        this.weekStart = weekStart;
        this.totalHoursWorked = totalHoursWorked;
    }


    public int getWeeklyId() {return weeklyId;}
    public void setWeeklyId(int weeklyId) {this.weeklyId = weeklyId;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public LocalDate getWeekStart() {return weekStart;}
    public void setWeekStart(LocalDate weekStart) {this.weekStart = weekStart;}
    public LocalTime getTotalHoursWorked() {return totalHoursWorked;}
    public void setTotalHoursWorked(LocalTime totalHoursWorked) {this.totalHoursWorked = totalHoursWorked;}
}