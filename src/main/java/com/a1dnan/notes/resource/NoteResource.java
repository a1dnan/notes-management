package com.a1dnan.notes.resource;

import com.a1dnan.notes.domain.HttpResponse;
import com.a1dnan.notes.domain.Note;
import com.a1dnan.notes.enums.Level;
import com.a1dnan.notes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteResource {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<HttpResponse<Note>> getNotes(){
        return ResponseEntity.ok().body(noteService.getNotes());
    }

    @PostMapping
    public ResponseEntity<HttpResponse<Note>> saveNote(@RequestBody @Valid Note note){
        return ResponseEntity.created(
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note").toUriString())
        ).body(noteService.saveNote(note));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam @Valid Level level){
        return ResponseEntity.ok().body(noteService.filterNotes(level));
    }

    @PutMapping()
    public ResponseEntity<HttpResponse<Note>> updateNote(@RequestBody @Valid Note note){
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<HttpResponse<Note>> deleteNote(@PathVariable("noteId") Long id){
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }

}
