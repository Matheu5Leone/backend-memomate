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

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID noteId, @RequestParam UUID userId){
        Note note = noteService.getNoteById(noteId);
        Optional<User> optionalUser = userService.findUserById(userId);
        List<UUID> uuid = new ArrayList();
        uuid.add(userId);

        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!note.getOwner().getId().equals(userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        noteService.deleteOrUnlinkNotesFromUser(optionalUser.get(), uuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/all/{userId}")
    public ResponseEntity<Void> deleteNotesFromUser(@PathVariable UUID userId, @RequestBody List<UUID> notesIds){
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        noteService.deleteOrUnlinkNotesFromUser(optionalUser.get(), notesIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Note>> listUserNotes(@PathVariable UUID userId){
        List<Note> notes = noteService.listAllByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }
}
