package memo.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import memo.backend.model.Note;
import memo.backend.model.User;
import memo.backend.repository.NoteRepository;
import memo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Transactional
    public void deleteNote(UUID noteId, UUID userId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (noteOptional.isPresent() && userOptional.isPresent()) {
            User user = userOptional.get();
            Note note = noteOptional.get();

            user.getNotes().remove(note);
            userRepository.save(user);
            note.setOwner(null);
            note.setUsers(null); // Remove todas as referências de usuários
            noteRepository.save(note); // Salva as alterações antes de deletar
            noteRepository.deleteById(noteId); // Deleta a nota
        } else {
            throw new EntityNotFoundException("Nota não encontrada com o ID: " + noteId);
        }
    }

    public List<Note> listAllByUser(UUID userId) {
        return noteRepository.findAllByUser(userId);
    }
}
