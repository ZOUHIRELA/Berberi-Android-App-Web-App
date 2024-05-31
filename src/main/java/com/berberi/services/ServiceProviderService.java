package com.berberi.services;

import com.berberi.dtos.AuthenticationRequest;
import com.berberi.dtos.AuthenticationResponse;
import com.berberi.dtos.ServiceProviderRequest;
import com.berberi.emails.EmailService;
import com.berberi.config.JwtService;
import com.berberi.model.Role;
import com.berberi.model.ServiceProvider;
import com.berberi.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {

    private final ServiceProviderRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final OAuth2TokenService oauth2TokenService;

    // Handle Google OAuth2 code
    public void handleGoogleOAuth2Code(String code) {
        oauth2TokenService.processGoogleOAuth2Token(code);
    }

    // Handle Facebook OAuth2 code
    public void handleFacebookOAuth2Code(String code) {
        oauth2TokenService.processFacebookOAuth2Token(code);
    }

    // Generate a random verification code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // Register a new service provider
    public AuthenticationResponse register(ServiceProviderRequest request) throws IOException {
        String verificationCode = generateVerificationCode();

        ServiceProvider serviceProvider = ServiceProvider.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .location(request.getLocation())
                .verificationCode(verificationCode)
                .role(Role.SERVICE_PROVIDER)
                .codeGeneratedTime(new Date())
                .verified(false)
                .build();

        repository.save(serviceProvider);

        emailService.sendVerificationCode(serviceProvider.getEmail(), verificationCode);

        String jwtToken = jwtService.generateToken(serviceProvider);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Verify the provided code
    public boolean verifyCode(String email, String code) {
        Optional<ServiceProvider> serviceProvider = repository.findByEmail(email);
        if (serviceProvider.isPresent()) {
            ServiceProvider service = serviceProvider.get();
            String verificationCode = service.getVerificationCode();
            if (verificationCode != null && verificationCode.equals(code)) {
                if (isCodeValid(service.getCodeGeneratedTime())) {
                    service.setVerified(true);
                    repository.save(service);
                    return true;
                }
            }
        }
        return false;
    }

    // Check if the verification code is still valid
    private boolean isCodeValid(Date codeGeneratedTime) {
        long currentTime = System.currentTimeMillis();
        long codeGeneratedMillis = codeGeneratedTime.getTime();
        return (currentTime - codeGeneratedMillis) <= (15 * 60 * 1000); // 15 minutes
    }

    // Authenticate a service provider
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        ServiceProvider serviceProvider = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        String jwtToken = jwtService.generateToken(serviceProvider);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Send verification code to service provider's email
    public void sendVerificationCode(String email) {
        Optional<ServiceProvider> optionalServiceProvider = repository.findByEmail(email);
        if (optionalServiceProvider.isPresent()) {
            ServiceProvider serviceProvider = optionalServiceProvider.get();
            String verificationCode = generateVerificationCode();
            serviceProvider.setVerificationCode(verificationCode);
            serviceProvider.setCodeGeneratedTime(new Date());
            repository.save(serviceProvider);
            emailService.sendVerificationCode(email, verificationCode);
        }
    }

    // Reset password for a service provider
    public void resetPassword(String email, String newPassword) {
        Optional<ServiceProvider> optionalServiceProvider = repository.findByEmail(email);
        if (optionalServiceProvider.isPresent()) {
            ServiceProvider serviceProvider = optionalServiceProvider.get();
            serviceProvider.setPassword(passwordEncoder.encode(newPassword));
            serviceProvider.setVerificationCode(null);
            serviceProvider.setCodeGeneratedTime(null);
            repository.save(serviceProvider);
        }
    }
}
