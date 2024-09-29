package memo.backend.controller;

import jakarta.validation.Valid;
import memo.backend.model.User;
import memo.backend.model.dto.ChangePasswordDto;
import memo.backend.model.dto.UserBase64AvatarDto;
import memo.backend.model.dto.UserLoginDto;
import memo.backend.model.dto.UserRegisterDto;
import memo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        User user = userService.registerUser(userRegisterDto);
        if (Objects.isNull(user))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserBase64AvatarDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        User user = userService.findUserByEmail(userLoginDto.getEmail());
        if (Objects.isNull(user))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        boolean validPassword = userService.verifyPassword(user, userLoginDto.getPassword());
        if (!validPassword)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserBase64AvatarDto userBase64AvatarDto = userService.createUserBase64Dto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userBase64AvatarDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam UUID userId){
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteUser(optionalUser.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/avatar/{userId}")
    public ResponseEntity<byte[]>  getAvatar(@PathVariable UUID userId){
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(optionalUser.get().getAvatar());
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        return userService.changePassword(changePasswordDto) ?
                ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PutMapping("/avatar/{userId}")
    public ResponseEntity<Void> updateAvatar(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile avatarFile) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        try {
            byte[] avatarBytes = avatarFile.getBytes();
            userService.updateUserAvatar(userId, avatarBytes);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
