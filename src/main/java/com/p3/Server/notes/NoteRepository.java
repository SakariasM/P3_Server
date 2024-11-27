package com.p3.Server.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT COUNT(n) > 0 FROM Note n WHERE n.note_date = :noteDate AND n.writer_id = :userId")
    boolean existsByDateAndUser(@Param("noteDate") LocalDate noteDate, @Param("userId") int userId);
}
