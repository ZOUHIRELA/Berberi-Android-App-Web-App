package com.berberi.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping("/update")
    public ResponseEntity<String> updateUserProfile(
            @RequestParam("currentEmail") String currentEmail,
            @RequestParam("newEmail") String newEmail,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        String responseMessage = userProfileService.requestProfileUpdate(currentEmail, newEmail, fullName, phoneNumber, profilePicture);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/confirmUpdate")
    public ResponseEntity<String> confirmUserProfileUpdate(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode) {
        String responseMessage = userProfileService.confirmProfileUpdate(email, verificationCode);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/cancelUpdate")
    public ResponseEntity<String> cancelUserProfileUpdate(
            @RequestParam("email") String email) {
        userProfileService.cancelProfileUpdate(email);
        return ResponseEntity.ok("Profile update canceled.");
    }

    @GetMapping
    public String getUserProfile(){
        return "hello Mister to your profile";
    }
}
