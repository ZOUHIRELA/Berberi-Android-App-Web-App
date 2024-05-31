package com.berberi.services;

import com.berberi.emails.EmailService;
import com.berberi.model.User;
import com.berberi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserProfilePicture(int userId, MultipartFile profilePicture) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(profilePicture.getBytes());
            user.setProfilePicture(base64Image);
        } else {
            throw new RuntimeException("Invalid profile picture");
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updateUserProfile(String currentEmail, String newEmail, String fullName, String phoneNumber, MultipartFile profilePicture) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmail(currentEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (newEmail != null) user.setEmail(newEmail);
            if (fullName != null) user.setFullName(fullName);
            if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(profilePicture.getBytes());
                user.setProfilePicture(base64Image);
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }
}
