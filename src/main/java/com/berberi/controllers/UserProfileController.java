package com.berberi.controllers;

import com.berberi.services.OAuth2TokenService;
import com.berberi.model.Role;
import com.berberi.model.User;
import com.berberi.services.UserProfileService;
import com.berberi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final OAuth2TokenService oAuth2TokenService;

    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestParam(value = "newEmail", required = false) String newEmail,
                                               @RequestParam(value = "fullName", required = false) String fullName,
                                               @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                               @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentEmail = userDetails.getUsername();

            User updatedUser = userProfileService.updateUserProfile(currentEmail, newEmail, fullName, phoneNumber, profilePicture);

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Optional<User> optionalUser = userRepository.findByEmail(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @GetMapping("/oauth2-profile")
    public ResponseEntity<?> getOAuth2UserProfile(@RequestParam String registrationId, @RequestParam String accessToken) {
        // Récupérer le profil de l'utilisateur OAuth2.0 avec OAuth2TokenService
        OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null);
        OAuth2User oAuth2User = oAuth2TokenService.getOAuth2User(registrationId, token);
        if (oAuth2User != null) {
            User user = createUserFromOAuth2User(oAuth2User);

            // Redirection vers /oauth2-profile
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/api/v1/profile/oauth2-profile")
                    .body(user);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve OAuth2 user profile");
        }
    }


    private User createUserFromOAuth2User(OAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setEmail(oAuth2User.getAttribute("email"));
        newUser.setFullName(oAuth2User.getAttribute("name"));
        newUser.setPhoneNumber(oAuth2User.getAttribute("phone_number"));
        newUser.setRole(Role.USER);
        newUser.setPassword("");
        newUser.setVerified(true);

        return newUser;
    }
}
