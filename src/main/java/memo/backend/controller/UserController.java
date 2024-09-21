package memo.backend.controller;

import jakarta.validation.Valid;
import memo.backend.model.User;
import memo.backend.model.dto.UserLoginDto;
import memo.backend.model.dto.UserRegisterDto;
import memo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
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

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        User user = userService.findUserByEmail(userLoginDto.getEmail());
        if (Objects.isNull(user))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        boolean validPassword = userService.verifyPassword(user, userLoginDto.getUserPassword());
        if (!validPassword)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam UUID userId){
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteUser(optionalUser.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
