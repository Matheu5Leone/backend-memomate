package memo.backend.service;

import memo.backend.model.User;
import memo.backend.model.dto.UserRegisterDto;
import memo.backend.repository.UserRepository;
import memo.backend.util.ProfilePictureGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
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
    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public User registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail()))
            return null;
        String encryptedPassword = passwordEncoder.encode(userRegisterDto.getPassword());

        // AVATAR GENERATOR BLOCK
        String fullName = userRegisterDto.getFirstName() + userRegisterDto.getLastName();
        String initials = ProfilePictureGenerator.generateLetters(fullName);
        BufferedImage bufferedImage = ProfilePictureGenerator.generateProfilePicture(initials);
        byte[] bytesAvatar = ProfilePictureGenerator.convertBufferedImageToByteArray(bufferedImage);
        //

        User newUser = new User(
                    userRegisterDto.getFirstName(),
                    userRegisterDto.getLastName(),
                    userRegisterDto.getBirthdate(),
                    userRegisterDto.getEmail(),
                    encryptedPassword,
                    bytesAvatar
                );
        return userRepository.save(newUser);
    }

    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
