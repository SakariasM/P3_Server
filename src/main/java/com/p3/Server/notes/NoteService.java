package com.p3.Server.notes;

import com.p3.Server.timelog.Timelog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(int id) {
        return noteRepository.findById(id);
    }

    public Note addNewNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNoteById(int id) {
        if (!noteRepository.existsById(id)) {
            throw new IllegalStateException("Note with ID " + id + " does not exist.");
        }
        noteRepository.deleteById(id);
    }

    public boolean noteExistsForDateAndUser(LocalDate noteDate, int userId) {
        return noteRepository.existsByDateAndUser(noteDate, userId);
    }

    public List<List<Note>> getWeekNotes(LocalDate date, int userId) {
        LocalDate weekStart = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Note> weekNotes = noteRepository.findByUserIdAndWeek(userId, weekStart, weekEnd);
        List<List<Note>> weekNotesGroupedByDay = new ArrayList<>();

        for(int i = 0; i < 7; i++){     // O(n^2) not good :( but shit, it works    For each day in the week
            LocalDate currentDay = weekStart.plusDays(i);
            List<Note> dailyTimelogs = new ArrayList<>();

            for(Note note : weekNotes){
                if(note.getNote_date().isEqual(currentDay)){
                    dailyTimelogs.add(note);
                }
            }
            weekNotesGroupedByDay.add(dailyTimelogs);
        }

        return weekNotesGroupedByDay;
    }



}
