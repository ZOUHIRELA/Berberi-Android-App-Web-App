package com.berberi.controllers;

import com.berberi.dto.AuthenticationRequest;
import com.berberi.dto.AuthenticationResponse;
import com.berberi.services.AuthenticationService;
import com.berberi.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        service.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent to email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String newPassword,
                                                @RequestParam String confirmPassword,
                                                @RequestParam String verificationCode) {
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas.");
        }
        boolean isCodeValid = service.verifyCode(email, verificationCode);
        if (!isCodeValid) {
            return ResponseEntity.badRequest().body("Code de vérification incorrect ou expiré.");
        }
        service.resetPassword(email, newPassword);

        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        if (service.verifyCode(email, code)) {
            return ResponseEntity.ok("Code verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code or expired.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/oauth2/facebook")
    public ResponseEntity<Void> processFacebookOAuth2Code(@RequestParam String code) {
        service.handleFacebookOAuth2Code(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth2/google")
    public ResponseEntity<Void> processGoogleOAuth2Code(@RequestParam String code) {
        service.handleGoogleOAuth2Code(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }
}