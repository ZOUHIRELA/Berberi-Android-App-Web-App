package com.berberi.dtos;

import lombok.Data;

@Data
public class ServiceProviderRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String location;
}
