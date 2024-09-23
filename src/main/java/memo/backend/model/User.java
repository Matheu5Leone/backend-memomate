package memo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    @Lob // Specifies that this field should be treated as a BLOB in the database
    @Column(name = "avatar", columnDefinition = "BLOB")
    private byte[] avatar;

    public User(String firstName, String lastName, Date birthdate, String email, String password, byte[] avatar) {
        this.userId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

}
