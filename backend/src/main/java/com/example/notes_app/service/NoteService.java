package com.example.notes_app.service;

import com.example.notes_app.dto.NoteDto;

import java.util.List;

public interface NoteService {
    NoteDto createNote(NoteDto noteDto);
    NoteDto getNoteById(Long id);
    List<NoteDto> getAllNotes();
    NoteDto updateNote(Long id, NoteDto noteDto);
    void deleteNote(Long id);
}
