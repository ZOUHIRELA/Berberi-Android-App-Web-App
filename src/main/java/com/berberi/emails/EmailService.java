package com.berberi.emails;

public interface EmailService {

    void sendVerificationCode(String email, String code);

}
