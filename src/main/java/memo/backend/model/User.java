package memo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String email;
    private String password;
    @Lob
    @Column(name = "avatar", columnDefinition = "BLOB")
    private byte[] avatar;

    @ManyToMany
    @JoinTable(
            name = "user_notes",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "note")
    )
    private List<Note> notes = new ArrayList<>();

    public User(String firstName, String lastName, Date birthdate, String email, String password, byte[] avatar) {
        this.userId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.notes = new ArrayList<>();
    }
}
