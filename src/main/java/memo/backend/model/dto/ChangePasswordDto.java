package memo.backend.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangePasswordDto {
    private UUID id;
    private String password;
    private String confirmNewPassword;
    private String newPassword;

    public ChangePasswordDto(UUID id, String password, String confirmPassword, String newPassword) {
        this.id = id;
        this.password = password;
        this.confirmNewPassword = confirmPassword;
        this.newPassword = newPassword;
    }
}
