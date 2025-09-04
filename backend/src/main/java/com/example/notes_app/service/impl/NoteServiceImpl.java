package com.example.notes_app.service.impl;

import com.example.notes_app.dto.NoteDto;
import com.example.notes_app.entity.NoteEntity;
import com.example.notes_app.repository.NoteRepository;
import com.example.notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public NoteDto createNote(NoteDto noteDto) {
        NoteEntity noteEntity = NoteEntity.builder()
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return mapToDto(noteRepository.save(noteEntity));
    }

    @Override
    public NoteDto getNoteById(Long id) {
        return noteRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));
    }

    @Override
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto updateNote(Long id, NoteDto noteDto) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));
        noteEntity.setTitle(noteDto.getTitle());
        noteEntity.setContent(noteDto.getContent());
        noteEntity.setUpdatedAt(LocalDateTime.now());
        return mapToDto(noteRepository.save(noteEntity));
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    private NoteDto mapToDto(NoteEntity noteEntity) {
        return NoteDto.builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .content(noteEntity.getContent())
                .build();
    }
}
