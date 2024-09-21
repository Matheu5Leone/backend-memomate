package memo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email;

    private String userName;

    private String userPassword;

    public User(String email, String userName, String userPassword) {
        this.userId = UUID.randomUUID();
        this.email = email;
        this.userName = userName;
        this.userPassword = userPassword;
    }

}
