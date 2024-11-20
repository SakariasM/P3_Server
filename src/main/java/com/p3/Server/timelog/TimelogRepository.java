package com.p3.Server.timelog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimelogRepository extends JpaRepository<Timelog, Integer> {

    // Return row of timelog where month and year match parameters
    @Query("SELECT t FROM Timelog t WHERE MONTH(t.shift_date) = :month AND YEAR(t.shift_date) = :year")
    List<Timelog> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT t FROM Timelog t WHERE t.user_id = :user_id AND DATE(t.shift_date) = :shift_date")
    List<Timelog> findByIdAndDay(@Param("user_id") int user_id, @Param("shift_date") LocalDate shift_date);

}
