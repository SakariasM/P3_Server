package com.p3.Server.weeklyTimelog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeeklyTimelogRepository extends JpaRepository<WeeklyTimelog, Integer> {

    //@Query("SELECT w FROM WeeklyTimelog w WHERE DATE(w.week_start) = :weekStart AND w.user_id = :userId");
    Optional<WeeklyTimelog> findWeeklyTimelogByWeekStartAndUserId(LocalDate weekStart, int userId);

}