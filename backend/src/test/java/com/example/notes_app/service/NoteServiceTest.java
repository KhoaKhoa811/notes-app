package com.example.notes_app.service;

import com.example.notes_app.dto.NoteDto;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.NoteEntity;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.NoteRepository;
import com.example.notes_app.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private AccountRepository accountRepository;

    private AccountEntity mockAccount;
    private NoteEntity mockNote;

    @BeforeEach
    void setUp() {
        // Mock account
        mockAccount = AccountEntity.builder()
                .id(1)
                .email("user@example.com")
                .enabled(true)
                .build();

        // Mock SecurityContext to simulate logged-in user
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@example.com");

        SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock accountRepository
        when(accountRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockAccount));

        // Mock NoteEntity
        mockNote = NoteEntity.builder()
                .id(1L)
                .title("Test Note")
                .content("Test Content")
                .account(mockAccount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateNote() {
        when(noteRepository.save(any(NoteEntity.class))).thenReturn(mockNote);

        NoteDto dto = NoteDto.builder().title("Test Note").content("Test Content").build();
        NoteDto result = noteService.createNote(dto);

        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
    }

    @Test
    void testGetNoteById() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(mockNote));

        NoteDto result = noteService.getNoteById(1L);
        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
    }

    @Test
    void testGetAllNotes() {
        // Mock repo theo method thật của service
        when(noteRepository.findByAccount_Id(mockAccount.getId()))
                .thenReturn(Collections.singletonList(mockNote));

        List<NoteDto> notes = noteService.getAllNotes();
        assertEquals(1, notes.size());
        assertEquals("Test Note", notes.getFirst().getTitle());
    }


    @Test
    void testUpdateNote() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(mockNote));
        when(noteRepository.save(any(NoteEntity.class))).thenReturn(mockNote);

        NoteDto updateDto = NoteDto.builder().title("Updated Note").content("Updated Content").build();
        NoteDto updated = noteService.updateNote(1L, updateDto);

        assertNotNull(updated);
        assertEquals("Updated Note", updated.getTitle());
    }

    @Test
    void testDeleteNote_Success() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(mockNote));

        // deleteNote trả về void, chỉ cần đảm bảo không ném lỗi
        assertDoesNotThrow(() -> noteService.deleteNote(1L));
    }
}
