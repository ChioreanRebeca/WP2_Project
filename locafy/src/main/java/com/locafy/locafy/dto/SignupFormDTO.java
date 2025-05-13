package com.locafy.locafy.dto;

import lombok.Data;

@Data
public class SignupFormDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String role; // "LOCAL" or "BUSINESS_OWNER"

}
