package com.berberi.profile;

import org.springframework.web.multipart.MultipartFile;

public class TempProfileUpdate {
    private String email;
    private String fullName;
    private String phoneNumber;
    private MultipartFile photo;

    public TempProfileUpdate(String email, String fullName, String phoneNumber, MultipartFile photo) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
