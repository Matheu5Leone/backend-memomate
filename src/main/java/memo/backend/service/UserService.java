package memo.backend.service;

import memo.backend.model.User;
import memo.backend.model.dto.UserRegisterDto;
import memo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail()))
            return null;
        String encryptedPassword = passwordEncoder.encode(userRegisterDto.getUserPassword());
        User newUser = new User(userRegisterDto.getEmail(), userRegisterDto.getUserName(), encryptedPassword);
        return userRepository.save(newUser);
    }

    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getUserPassword());
    }

    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
