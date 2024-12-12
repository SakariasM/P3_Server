package com.p3.Server.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private final NoteService noteService;
    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /*
    *           Get requests
     */

    @GetMapping("/exists")
    public boolean noteExists(@RequestParam LocalDate noteDate, @RequestParam int userId) {
        return noteService.noteExistsForDateAndUser(noteDate, userId);
    }

    @GetMapping("/week/history")
    public List<List<Note>> getWeekNotes(@RequestParam LocalDate date, @RequestParam int userId) {
        return noteService.getWeekNotes(date, userId);
    }

    @GetMapping("/day")
    public List<Note> getDayNotes(@RequestParam LocalDate date, @RequestParam int userId) {
        return noteService.getDayNotes(date, userId);
    }


    /*
     *           Post requests
     */


    @PostMapping("/addNewNote")
    public Note addNewNote(@RequestBody Note note) {
        return noteService.addNewNote(note);
    }
}
