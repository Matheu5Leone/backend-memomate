package memo.backend.service;

import memo.backend.model.User;
import memo.backend.model.dto.ChangePasswordDto;
import memo.backend.model.dto.UserBase64AvatarDto;
import memo.backend.model.dto.UserRegisterDto;
import memo.backend.repository.UserRepository;
import memo.backend.util.ProfilePictureGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Base64;
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

    public UserBase64AvatarDto createUserBase64Dto(User user) {
        String base64Image = Base64.getEncoder().encodeToString(user.getAvatar());
        return new UserBase64AvatarDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail(),
                base64Image
        );
    }

    public boolean changePassword(ChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword()))
            return false;

        return userRepository.findById(changePasswordDto.getId())
                .filter(user -> passwordEncoder.matches(changePasswordDto.getPassword(), user.getPassword()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public void updateUserAvatar(UUID userId, byte[] avatar) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAvatar(avatar);
            userRepository.save(user);
        }
    }
}
