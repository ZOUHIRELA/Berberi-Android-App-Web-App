package com.berberi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ServiceProviderRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String location;
}
