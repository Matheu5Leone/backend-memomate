package memo.backend.controller;

import memo.backend.model.User;
import memo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/memomate")
public class MemomateController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity <List<User>> listAll(){
        List<User> users = userService.listUsers();
        if(users.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
