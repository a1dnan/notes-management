package com.a1dnan.notes.repo;

import com.a1dnan.notes.domain.Note;
import com.a1dnan.notes.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByLevel(Level level);

}
