package com.example.notes_app.repository;

import com.example.notes_app.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findByAccount_Id(Integer accountId);
}
