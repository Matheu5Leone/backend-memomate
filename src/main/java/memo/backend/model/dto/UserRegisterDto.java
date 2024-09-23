package memo.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @Size(min = 3, message = "The first name must have at least 3 characters")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Size(min = 3, message = "The last name must have at least 3 characters")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Past(message = "Birthdate needs to be in past time")
    private Date birthdate;

    @Email(message = "The email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "The password must have at least 8 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
