package memo.backend.controller;

import memo.backend.model.Note;
import memo.backend.model.User;
import memo.backend.model.dto.NewNoteDto;
import memo.backend.service.NoteService;
import memo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NewNoteDto newNoteDto) {
        Optional<User> optionalUser = userService.findUserById(newNoteDto.getOwner());
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User owner = optionalUser.get();
        Note newNote = new Note(owner);
        Note createdNote = noteService.createNote(newNote);
        owner.getNotes().add(createdNote);
        userService.saveUser(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @PutMapping("/{noteId}/title")
    public ResponseEntity<Note> updateNoteTitle(@PathVariable UUID noteId, @RequestBody UUID userId, @RequestBody String newTitle) {
        if (!noteService.userHasAccessToNote(userId, noteId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Note updatedNote = noteService.updateNoteTitle(noteId, newTitle);
        return ResponseEntity.status(HttpStatus.OK).body(updatedNote);
    }

    @PutMapping("/{noteId}/content")
    public ResponseEntity<Note> updateNoteContent(@PathVariable UUID noteId, @RequestBody UUID userId, @RequestBody String newContent) {
        if (!noteService.userHasAccessToNote(userId, noteId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Note updatedNote = noteService.updateNoteContent(noteId, newContent);
        return ResponseEntity.status(HttpStatus.OK).body(updatedNote);
    }

    @DeleteMapping("/{noteId}/user/{userId}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID noteId, @PathVariable UUID userId){
        noteService.deleteNote(noteId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Note>> listUserNotes(@PathVariable UUID userId){
        List<Note> notes = noteService.listAllByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }
}
