package com.berberi.services;

import com.berberi.model.User;
import com.berberi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

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
}
