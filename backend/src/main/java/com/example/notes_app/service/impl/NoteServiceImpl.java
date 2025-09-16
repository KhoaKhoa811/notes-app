package com.example.notes_app.service.impl;

import com.example.notes_app.dto.NoteDto;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.NoteEntity;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.NoteRepository;
import com.example.notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final AccountRepository accountRepository;

    @Override
    public NoteDto createNote(NoteDto noteDto) {
        AccountEntity account = getCurrentAccount();

        NoteEntity noteEntity = NoteEntity.builder()
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .account(account)
                .build();

        return mapToDto(noteRepository.save(noteEntity));
    }

    @Override
    public NoteDto getNoteById(Long id) {
        AccountEntity account = getCurrentAccount();

        NoteEntity note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));

        if (!note.getAccount().getId().equals(account.getId())) {
            throw new RuntimeException("You are not allowed to view this note");
        }

        return mapToDto(note);
    }

    @Override
    public List<NoteDto> getAllNotes() {
        AccountEntity account = getCurrentAccount();

        return noteRepository.findByAccount_Id(account.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto updateNote(Long id, NoteDto noteDto) {
        AccountEntity account = getCurrentAccount();

        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));

        if (!noteEntity.getAccount().getId().equals(account.getId())) {
            throw new RuntimeException("You are not allowed to update this note");
        }

        noteEntity.setTitle(noteDto.getTitle());
        noteEntity.setContent(noteDto.getContent());
        noteEntity.setUpdatedAt(LocalDateTime.now());

        return mapToDto(noteRepository.save(noteEntity));
    }

    @Override
    public void deleteNote(Long id) {
        AccountEntity account = getCurrentAccount();

        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));

        if (!noteEntity.getAccount().getId().equals(account.getId())) {
            throw new RuntimeException("You are not allowed to delete this note");
        }

        noteRepository.delete(noteEntity);
    }

    // Helper method: Lấy account hiện tại từ token
    private AccountEntity getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // do JWT filter set email vào đây
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found with email " + email));
    }

    private NoteDto mapToDto(NoteEntity noteEntity) {
        return NoteDto.builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .content(noteEntity.getContent())
                .build();
    }
}
