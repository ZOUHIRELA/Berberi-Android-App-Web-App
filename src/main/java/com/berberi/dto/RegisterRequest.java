package com.berberi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private MultipartFile profilePicture; // Ajout de la propriété pour l'image de profil
}
