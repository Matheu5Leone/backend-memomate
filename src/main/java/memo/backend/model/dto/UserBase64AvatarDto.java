package memo.backend.model.dto;

import java.util.Date;
import java.util.UUID;

public class UserBase64AvatarDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String email;
    private String avatar;

    public UserBase64AvatarDto(UUID id, String firstName, String lastName, Date birthdate, String email, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.avatar = avatar;
    }
}
