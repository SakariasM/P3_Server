package com.p3.Server.notes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int note_id;
    private LocalDate note_date;
    private int writer_id;
    private int recipient_id;
    private String full_name;
    private String written_note;

    public Note() {

    }

    public Note(int note_id, LocalDate note_date, int writer_id, int recipient_id, String full_name, String written_note) {
        this.note_id = note_id;
        this.note_date = note_date;
        this.writer_id = writer_id;
        this.recipient_id = recipient_id;
        this.full_name = full_name;
        this.written_note = written_note;
    }

    public Note(LocalDate note_date, int writer_id, int recipient_id, String full_name, String written_note) {
        this.note_date = note_date;
        this.writer_id = writer_id;
        this.recipient_id = recipient_id;
        this.full_name = full_name;
        this.written_note = written_note;
    }

    public int getNote_id() {return note_id;}
    public void setNote_id(int noteId) {this.note_id = noteId;}
    public LocalDate getNote_date() {return note_date;}
    public void setNote_date(LocalDate noteDate) {this.note_date = noteDate;}
    public int getWriter_id() {return writer_id;}
    public void setWriter_id(int writerId) {this.writer_id = writerId;}
    public int getRecipient_id() {return recipient_id;}
    public void setRecipient_id(int recipientId) {this.recipient_id = recipientId;}
    public String getFull_name() {return full_name;}
    public void setFull_name(String fullName) {this.full_name = fullName;}
    public String getWritten_note() {return written_note;}
    public void setWritten_note(String writtenNote) {this.written_note = writtenNote;}

    @Override       // Override as toJSON?
    public String toString() {
        return "timelog{" +
                "note_id=" + note_id +
                ", note_date=" + note_date +
                ", writer_id=" + writer_id +
                ", recipient_id=" + recipient_id +
                ", full_name=" + full_name +
                ", written_note=" + written_note +
                "}";
    }

}
