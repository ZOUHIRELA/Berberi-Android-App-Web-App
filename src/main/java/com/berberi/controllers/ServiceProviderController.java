package com.berberi.controllers;

import com.berberi.services.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.berberi.dtos.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/service-provider")
@RequiredArgsConstructor
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        serviceProviderService.sendVerificationCode(email);
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
        boolean isCodeValid = serviceProviderService.verifyCode(email, verificationCode);
        if (!isCodeValid) {
            return ResponseEntity.badRequest().body("Code de vérification incorrect ou expiré.");
        }
        serviceProviderService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        if (serviceProviderService.verifyCode(email, code)) {
            return ResponseEntity.ok("Code verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code or expired.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody ServiceProviderRequest request) throws IOException {
        return ResponseEntity.ok(serviceProviderService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(serviceProviderService.authenticate(request));
    }

    @GetMapping("/oauth2/facebook")
    public ResponseEntity<Void> processFacebookOAuth2Code(@RequestParam String code) {
        serviceProviderService.handleFacebookOAuth2Code(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth2/google")
    public ResponseEntity<Void> processGoogleOAuth2Code(@RequestParam String code) {
        serviceProviderService.handleGoogleOAuth2Code(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }
}