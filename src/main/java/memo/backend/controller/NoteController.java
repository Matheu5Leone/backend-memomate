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

        Note newNote = new Note(optionalUser.get());
        Note createdNote = noteService.createNote(newNote);
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

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID noteId, @RequestBody UUID userId){
        Note note = noteService.getNoteById(noteId);
        if (!note.getOwner().getUserId().equals(userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        noteService.deleteNote(noteId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
