package com.berberi.profile;

import com.berberi.auth.EmailService;
import com.berberi.user.User;
import com.berberi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TempStorageService tempStorageService;
    private final PasswordEncoder passwordEncoder;

    // Generate a random verification code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // Request to update the user's profile
    public String requestProfileUpdate(String currentEmail, String newEmail, String fullName, String phoneNumber, MultipartFile profilePicture) {
        Optional<User> optionalUser = userRepository.findByEmail(currentEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            user.setVerificationCodeGeneratedTime(new Date());
            userRepository.save(user);

            // Send verification email
            emailService.sendVerificationCode(user.getEmail(), verificationCode);

            // Store the temporary profile update
            tempStorageService.storeTemporaryProfileUpdate(user.getId(), newEmail, fullName, phoneNumber, profilePicture);

            return "Verification code sent to your email.";
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    // Confirm the profile update
    public String confirmProfileUpdate(String email, String code) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode().equals(code) && isCodeValid(user.getVerificationCodeGeneratedTime())) {
                TempProfileUpdate tempUpdate = tempStorageService.getTemporaryProfileUpdate(user.getId());

                user.setEmail(tempUpdate.getEmail());
                user.setFullName(tempUpdate.getFullName());
                user.setPhoneNumber(tempUpdate.getPhoneNumber());
                if (tempUpdate.getPhoto() != null) {
                    try {
                        user.setProfilePicture(tempUpdate.getPhoto().getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to update photo.", e);
                    }
                }

                user.setVerificationCode(null);
                user.setVerificationCodeGeneratedTime(null);
                userRepository.save(user);

                tempStorageService.clearTemporaryProfileUpdate(user.getId());

                return "Profile updated successfully.";
            } else {
                throw new RuntimeException("Invalid or expired verification code.");
            }
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    // Cancel the profile update
    public void cancelProfileUpdate(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            tempStorageService.clearTemporaryProfileUpdate(user.getId());
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    // Check if the verification code is still valid (15 minutes validity)
    private boolean isCodeValid(Date codeGeneratedTime) {
        long currentTime = System.currentTimeMillis();
        long codeGeneratedMillis = codeGeneratedTime.getTime();
        return (currentTime - codeGeneratedMillis) <= (15 * 60 * 1000);
    }
}
