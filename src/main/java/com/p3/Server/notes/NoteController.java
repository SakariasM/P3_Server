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

    @GetMapping
    public List<Note> getAllNotes() {   // Skal nok aldrig bruges, kopierede bare lige fra users
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Optional<Note> getNoteById(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    @GetMapping("/exists")
    public boolean noteExists(@RequestParam LocalDate noteDate, @RequestParam int userId) {
        return noteService.noteExistsForDateAndUser(noteDate, userId);
    }

    @PostMapping
    public Note addNewNote(@RequestBody Note note) {
        return noteService.addNewNote(note);
    }

    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable int id) {
        noteService.deleteNoteById(id);
    }

}
