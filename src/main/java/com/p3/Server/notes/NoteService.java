package com.p3.Server.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

}
