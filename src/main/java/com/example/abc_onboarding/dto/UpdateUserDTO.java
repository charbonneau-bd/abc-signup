package com.example.abc_onboarding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private String nationality;
    private String address;
    private String ssn;
    private Boolean onboarding; // use Boolean so it can be null for partial updates
    private byte[] identification;
}
