package memo.backend.service;

import memo.backend.model.Note;
import memo.backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public boolean userHasAccessToNote(UUID userId, UUID noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        if (note.getOwner().getUserId().equals(userId)) {
            return true;
        }
        return note.getUsers().stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    public Note updateNoteTitle(UUID noteId, String newTitle) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        note.setTitle(newTitle);
        return noteRepository.save(note);
    }

    public Note updateNoteContent(UUID noteId, String newContent) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        note.setContent(newContent);
        return noteRepository.save(note);
    }

    public Note getNoteById(UUID noteId) {
        return noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Nota não encontrada"));
    }

    public void deleteNote(UUID noteId) {
        noteRepository.deleteById(noteId);
    }
}
