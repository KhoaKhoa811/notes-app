package com.example.notes_app.controller;

import com.example.notes_app.dto.NoteDto;
import com.example.notes_app.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
    }

    @Test
    @WithMockUser
    void testCreateNote() throws Exception {
        NoteDto request = new NoteDto();
        request.setTitle("Test Note");
        request.setContent("This is a test note.");

        NoteDto response = new NoteDto();
        response.setId(1L);
        response.setTitle("Test Note");
        response.setContent("This is a test note.");

        Mockito.when(noteService.createNote(any(NoteDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Note"))
                .andExpect(jsonPath("$.data.content").value("This is a test note."));
    }

    @Test
    @WithMockUser
    void testGetNoteById() throws Exception {
        NoteDto response = new NoteDto();
        response.setId(1L);
        response.setTitle("Test Note");
        response.setContent("This is a test note.");

        Mockito.when(noteService.getNoteById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Note"))
                .andExpect(jsonPath("$.data.content").value("This is a test note."));
    }

    @Test
    @WithMockUser
    void testGetAllNotes() throws Exception {
        NoteDto note = new NoteDto();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("This is a test note.");

        Mockito.when(noteService.getAllNotes()).thenReturn(List.of(note));

        mockMvc.perform(get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Note"))
                .andExpect(jsonPath("$.data[0].content").value("This is a test note."));
    }

    @Test
    @WithMockUser
    void testUpdateNote() throws Exception {
        NoteDto request = new NoteDto();
        request.setTitle("Updated Note");
        request.setContent("Updated content.");

        NoteDto response = new NoteDto();
        response.setId(1L);
        response.setTitle("Updated Note");
        response.setContent("Updated content.");

        Mockito.when(noteService.updateNote(any(Long.class), any(NoteDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Updated Note"))
                .andExpect(jsonPath("$.data.content").value("Updated content."));
    }

    @Test
    @WithMockUser
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/v1/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
