package com.p3.Server.timelog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelogRepository extends JpaRepository<Timelog, Integer> {

    // Return row of timelog where month and year match parameters
    @Query("SELECT t FROM Timelog t WHERE MONTH(t.shift_date) = :month AND YEAR(t.shift_date) = :year")
    List<Timelog> findByMonthAndYear(@Param("month") int month, @Param("year") int year);


    // Find check-in events for today's date without a corresponding check-out event
    @Query("""
        SELECT t1 FROM Timelog t1
        WHERE t1.event_type = 'check_in'
          AND t1.shift_date = CURRENT_DATE
          AND NOT EXISTS (
            SELECT 1 FROM Timelog t2
            WHERE t2.user_id = t1.user_id
              AND t2.event_type = 'check_out'
              AND t2.shift_date = t1.shift_date
          )
    """)
    List<Timelog> findTodaysCheckInsWithoutCheckOuts();
}