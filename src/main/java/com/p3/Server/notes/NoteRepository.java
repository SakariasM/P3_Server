package com.p3.Server.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT COUNT(n) > 0 FROM Note n WHERE n.note_date = :noteDate AND n.writer_id = :userId")
    boolean existsByDateAndUser(@Param("noteDate") LocalDate noteDate, @Param("userId") int userId);

    @Query("SELECT n FROM Note n WHERE (n.writer_id = :userId OR n.recipient_id = :userId) AND n.note_date BETWEEN :weekStart AND :weekEnd")
    List<Note> findByUserIdAndWeek(@Param("userId") int userId,
                                   @Param("weekStart") LocalDate weekStart,
                                   @Param("weekEnd") LocalDate weekEnd);

    @Query("SELECT n FROM Note n WHERE (n.writer_id = :userId OR n.recipient_id = :userId) AND n.note_date = :date")
    List<Note> findByUserIdAndDate(@Param("userId") int userId,
                                   @Param("date") LocalDate date);
}
