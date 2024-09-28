package memo.backend.service;

import jakarta.transaction.Transactional;
import memo.backend.model.Note;
import memo.backend.model.User;
import memo.backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (note.getOwner().getId().equals(userId)) {
            return true;
        }
        return note.getUsers().stream()
                .anyMatch(user -> user.getId().equals(userId));
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

    public List<Note> listAllByUser(UUID userId) {
        return noteRepository.findAllByUser(userId);
    }

    @Transactional
    public void deleteOrUnlinkNotesFromUser(User user, List<UUID> notesIds) {
        List<Note> notes = noteRepository.findAllById(notesIds);

        for (Note note : notes) {
            if (note.getOwner().equals(user)) {
                noteRepository.delete(note);
            } else {
                note.getUsers().remove(user);
                noteRepository.save(note);
            }
        }
    }
}
