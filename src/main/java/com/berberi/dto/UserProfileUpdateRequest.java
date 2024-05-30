package com.berberi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private MultipartFile profilePicture;
}
