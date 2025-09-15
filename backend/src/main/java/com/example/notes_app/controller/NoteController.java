package com.example.notes_app.controller;

import com.example.notes_app.dto.NoteDto;
import com.example.notes_app.dto.ApiResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<ApiResponse<NoteDto>> createNote(@RequestBody NoteDto noteDto) {
        NoteDto createdNote = noteService.createNote(noteDto);
        ApiResponse<NoteDto> response = ApiResponse.<NoteDto>builder()
                .code(ErrorCode.NOTE_CREATED.getCode())
                .message(ErrorCode.NOTE_CREATED.getMessage())
                .data(createdNote)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteDto>> getNoteById(@PathVariable Long id) {
        NoteDto note = noteService.getNoteById(id);
        ApiResponse<NoteDto> response = ApiResponse.<NoteDto>builder()
                .code(ErrorCode.NOTE_GET_BY_ID.getCode())
                .data(note)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NoteDto>>> getAllNotes() {
        List<NoteDto> notes = noteService.getAllNotes();
        ApiResponse<List<NoteDto>> response = ApiResponse.<List<NoteDto>>builder()
                .code(ErrorCode.NOTE_GET_ALL.getCode())
                .data(notes)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteDto>> updateNote(
            @PathVariable Long id,
            @RequestBody NoteDto noteDto
    ) {
        NoteDto updatedNote = noteService.updateNote(id, noteDto);
        ApiResponse<NoteDto> response = ApiResponse.<NoteDto>builder()
                .code(ErrorCode.NOTE_UPDATED.getCode())
                .message(ErrorCode.NOTE_UPDATED.getMessage())
                .data(updatedNote)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.NOTE_DELETED.getCode())
                .message(ErrorCode.NOTE_DELETED.getMessage())
                .build();
        return ResponseEntity.ok(response);
    }
}
