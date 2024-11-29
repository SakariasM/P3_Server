package com.p3.Server.timelog;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Timelog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Så spring boot ved at det er mysql auto increment
    private int log_id;
    private int user_id;
    private LocalDate shift_date;  // LocalDate eller bare date? Så vidt jeg kan læse mig frem til er localdate en nyere version, men kender ikke helt forskellen.
    private LocalDateTime event_time;    // Time eller date?
    private String event_type;

    public Timelog(){

    }

    public Timelog(int log_id, int user_id, LocalDate shift_date, LocalDateTime event_time, String event_type) {
        this.log_id = log_id;
        this.user_id = user_id;
        this.shift_date = shift_date;
        this.event_time = event_time;
        this.event_type = event_type;
    }

    public Timelog(int user_id, LocalDate shift_date, LocalDateTime event_time, String event_type) {
        this.user_id = user_id;
        this.shift_date = shift_date;
        this.event_time = event_time;
        this.event_type = event_type;
    }

    @JsonProperty("log_id")
    public int getLog_id() { return log_id; }
    @JsonProperty("log_id")
    public void setLog_id(int log_id) { this.log_id = log_id; }

    @JsonProperty("user_id")
    public int getUser_id() { return user_id; }
    @JsonProperty("user_id")
    public void setUser_id(int user_id) { this.user_id = user_id; }

    @JsonProperty("shift_date")
    public LocalDate getShift_date() { return shift_date; }
    @JsonProperty("shift_date")
    public void setShift_date(LocalDate shift_date) { this.shift_date = shift_date; }

    @JsonProperty("event_time")
    public LocalDateTime getEvent_time() { return event_time; }
    @JsonProperty("event_time")
    public void setEvent_time(LocalDateTime event_time) { this.event_time = event_time; }

    @JsonProperty("event_type")
    public String getEvent_type() { return event_type; }
    @JsonProperty("event_type")
    public void setEvent_type(String event_type) { this.event_type = event_type; }

    @Override       // Override as toJSON?
    public String toString() {
        return "timelog{" +
                "log_id=" + log_id +
                ", user_id=" + user_id +
                ", shift_date=" + shift_date +
                ", event_time=" + event_time +
                ", event_type=" + event_type +
                "}";
    }

}