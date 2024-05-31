package com.berberi.services;

import com.berberi.emails.*;
import com.berberi.config.JwtService;
import com.berberi.dtos.AuthenticationRequest;
import com.berberi.dtos.AuthenticationResponse;
import com.berberi.dtos.RegisterRequest;
import com.berberi.model.Role;
import com.berberi.model.User;
import com.berberi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OAuth2TokenService oauth2TokenService;

    public void handleGoogleOAuth2Code(String code) {
        oauth2TokenService.processGoogleOAuth2Token(code);
    }

    public void handleFacebookOAuth2Code(String code) {
        oauth2TokenService.processFacebookOAuth2Token(code);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        String verificationCode = generateVerificationCode();

        User user = User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .verificationCode(verificationCode)
                .verificationCodeGeneratedTime(new Date())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), verificationCode);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean verifyCode(String email, String code) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String verificationCode = user.getVerificationCode();
            if (verificationCode != null && verificationCode.equals(code)) {
                if (isCodeValid(user.getVerificationCodeGeneratedTime())) {
                    user.setVerified(true);
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCodeValid(Date codeGeneratedTime) {
        long currentTime = System.currentTimeMillis();
        long codeGeneratedMillis = codeGeneratedTime.getTime();
        return (currentTime - codeGeneratedMillis) <= (15 * 60 * 1000);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void sendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            user.setVerificationCodeGeneratedTime(new Date());
            userRepository.save(user);
            emailService.sendVerificationCode(email, verificationCode);
        }
    }

    public void resetPassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setVerificationCode(null);
            user.setVerificationCodeGeneratedTime(null);
            userRepository.save(user);
        }
    }
}
