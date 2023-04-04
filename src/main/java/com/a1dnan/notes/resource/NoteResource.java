package com.a1dnan.notes.resource;

import com.a1dnan.notes.domain.HttpResponse;
import com.a1dnan.notes.domain.Note;
import com.a1dnan.notes.enums.Level;
import com.a1dnan.notes.service.NoteService;
import com.a1dnan.notes.util.DateUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

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

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request){
        return new ResponseEntity<>(
             HttpResponse.<Note>builder()
                     .reason("There is no mapping for a " + request.getMethod() + " request for this path")
                     .status(HttpStatus.NOT_FOUND)
                     .statusCode(HttpStatus.NOT_FOUND.value())
                     .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                     .build(),HttpStatus.NOT_FOUND);
    }

}
