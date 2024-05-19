package com.berberi.auth;

public interface EmailService {

    void sendVerificationCode(String email, String code);

}
