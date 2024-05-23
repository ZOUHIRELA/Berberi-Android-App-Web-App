package com.berberi.profile;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class TempProfileUpdate {
    private String email;
    private String fullName;
    private String phoneNumber;
    private byte[] photo;
    private String contentType;

    // Constructor, getters, and setters
    public TempProfileUpdate(String email, String fullName, String phoneNumber, MultipartFile photo) throws IOException {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.photo = photo.getBytes(); // Convert MultipartFile to byte[]
        this.contentType = photo.getContentType(); // Store content type
    }

    // Getters and setters
    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    // Other getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
