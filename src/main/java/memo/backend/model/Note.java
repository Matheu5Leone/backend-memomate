package memo.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import memo.backend.repository.NoteColorEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    public String title;
    public String content;
    private LocalDateTime dataCriacao;
    private String color;
    @ManyToMany(mappedBy = "notes")
    private List<User> users = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Note(User owner) {
        this.id = UUID.randomUUID();
        this.title = "";
        this.content = "";
        this.dataCriacao = LocalDateTime.now();
        this.color = NoteColorEnum.DEFAULT.getValue();
        this.owner = owner;
        this.users = new ArrayList<>();
        this.users.add(owner);
    }
}
