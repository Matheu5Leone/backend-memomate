package memo.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @Email(message = "The email must be valid")
    @NotBlank(message = "The email is required")
    private String email;

    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @NotBlank(message = "O nome é obrigatório")
    private String userName;

    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @NotBlank(message = "A senha é obrigatória")
    private String userPassword;
}
