package com.berberi.auth;

import com.berberi.user.Role;
import com.berberi.user.User;
import com.berberi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OAuth2TokenService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;
    private final DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

    @Transactional
    public void processGoogleOAuth2Token(String code) {
        OAuth2AccessToken accessToken = getOAuth2AccessToken("google", code);
        OAuth2User oAuth2User = getOAuth2User("google", accessToken);

        String email = oAuth2User.getAttribute("email");
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            updateUserFromOAuth2User(user, oAuth2User);
            userRepository.save(user);
        } else {
            User newUser = createUserFromOAuth2User(oAuth2User);
            userRepository.save(newUser);
            sendVerificationCode(newUser.getEmail(), newUser.getVerificationCode());
        }
    }

    @Transactional
    public void processFacebookOAuth2Token(String code) {
        OAuth2AccessToken accessToken = getOAuth2AccessToken("facebook", code);
        OAuth2User oAuth2User = getOAuth2User("facebook", accessToken);

        String email = oAuth2User.getAttribute("email");
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            updateUserFromOAuth2User(user, oAuth2User);
            userRepository.save(user);
        } else {
            User newUser = createUserFromOAuth2User(oAuth2User);
            userRepository.save(newUser);
            sendVerificationCode(newUser.getEmail(), newUser.getVerificationCode());
        }
    }

    private OAuth2AccessToken getOAuth2AccessToken(String registrationId, String code) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .redirectUri(clientRegistration.getRedirectUri())
                .scopes(clientRegistration.getScopes())
                .attributes(attrs -> attrs.put(OAuth2ParameterNames.REGISTRATION_ID, registrationId))
                .build();

        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse.success(code)
                .redirectUri(clientRegistration.getRedirectUri())
                .build();

        OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);

        OAuth2AuthorizationCodeGrantRequestEntityConverter converter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
        var requestEntity = converter.convert(new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange));

        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.exchange(
                clientRegistration.getProviderDetails().getTokenUri(),
                HttpMethod.POST,
                requestEntity,
                OAuth2AccessTokenResponse.class
        );

        return response.getBody().getAccessToken();
    }

    public OAuth2User getOAuth2User(String registrationId, OAuth2AccessToken accessToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
        return oAuth2UserService.loadUser(userRequest);
    }

    private User createUserFromOAuth2User(OAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setEmail(oAuth2User.getAttribute("email"));
        newUser.setFullName(oAuth2User.getAttribute("name"));
        newUser.setPhoneNumber(oAuth2User.getAttribute("phone_number")); // Assuming the phone number is available
        newUser.setRole(Role.USER);
        newUser.setPassword(""); // Set an empty password for OAuth users
        newUser.setVerified(false); // Mark the user as not verified initially
        newUser.setVerificationCode(generateVerificationCode()); // Generate verification code
        return newUser;
    }

    private void updateUserFromOAuth2User(User user, OAuth2User oAuth2User) {
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setFullName(oAuth2User.getAttribute("name"));
        user.setPhoneNumber(oAuth2User.getAttribute("phone_number")); // Assuming the phone number is available
        user.setRole(Role.USER);
        user.setPassword(""); // Set an empty password for OAuth users
        user.setVerified(false); // Mark the user as not verified initially
        user.setVerificationCode(generateVerificationCode()); // Generate verification code
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    private void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + code);
        emailSender.send(message);
    }
}
