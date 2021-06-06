package com.example.demo.service;

import com.example.demo.model.Notes;

import java.util.List;

public interface NoteService {

    int addNote(Notes note);
    void deleteNote(int id);
    void updateNote(Notes note);
    List<Notes> getAllNotes(int userId);
    Notes findNote(int id);
}
