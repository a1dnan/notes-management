package com.a1dnan.notes.service;

import com.a1dnan.notes.domain.HttpResponse;
import com.a1dnan.notes.domain.Note;
import com.a1dnan.notes.enums.Level;

public interface NoteService {

    HttpResponse<Note> getNotes();

    HttpResponse<Note> filterNotes(Level level);

    HttpResponse<Note> saveNote(Note note);

    HttpResponse<Note> updateNote(Note note);

    HttpResponse<Note> deleteNote(Long id);



}
