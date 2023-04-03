package com.a1dnan.notes.service.impl;

import com.a1dnan.notes.domain.HttpResponse;
import com.a1dnan.notes.domain.Note;
import com.a1dnan.notes.enums.Level;
import com.a1dnan.notes.exception.NoteNotFoundException;
import com.a1dnan.notes.repo.NoteRepository;
import com.a1dnan.notes.service.NoteService;
import com.a1dnan.notes.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepo;

    @Override
    public HttpResponse<Note> getNotes() {
        log.info("Fetching all the notes from the database");
        return HttpResponse.<Note>builder()
                .notes(noteRepo.findAll())
                .message(noteRepo.count() > 0 ? noteRepo.count() + " notes retrieved" : "No notes to display")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    @Override
    public HttpResponse<Note> filterNotes(Level level) {
        List<Note> notes = noteRepo.findByLevel(level);
        log.info("Fetching all the notes by level {}",level);
        return HttpResponse.<Note>builder()
                .notes(notes)
                .message(notes.size() + " notes are of" + level + " importance")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    @Override
    public HttpResponse<Note> saveNote(Note note) {
        log.info("Saving new note to the database");
        note.setCreatedAt(LocalDateTime.now());
        return HttpResponse.<Note>builder()
                .notes(Collections.singleton(noteRepo.save(note)))
                .message("Note created successfully")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    @Override
    public HttpResponse<Note> updateNote(Note note) {
        log.info("Updating note to the database");
        Optional<Note> opNote = Optional.ofNullable(noteRepo.findById(note.getId()))
                .orElseThrow(() -> new NoteNotFoundException("Nte not found"));
        Note updatedNote = opNote.get();
        updatedNote.setId(note.getId());
        updatedNote.setTitle(note.getTitle());
        updatedNote.setDescription(note.getDescription());
        updatedNote.setLevel(note.getLevel());
        noteRepo.save(updatedNote);
        return HttpResponse.<Note>builder()
                .notes(Collections.singleton(updatedNote))
                .message("Note updated successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    @Override
    public HttpResponse<Note> deleteNote(Long id) {
        log.info("Deleting note from the database");
        Optional<Note> opNote = Optional.ofNullable(noteRepo.findById(id))
                .orElseThrow(() -> new NoteNotFoundException("Nte not found"));
        opNote.ifPresent(noteRepo::delete);
        return HttpResponse.<Note>builder()
                .notes(Collections.singleton(opNote.get()))
                .message("Note deleted successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }
}
