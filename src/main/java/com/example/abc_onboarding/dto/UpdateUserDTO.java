package com.example.abc_onboarding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private Long id;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String gender;
    @JsonProperty
    private String dateOfBirth;
    @JsonProperty
    private String phoneNumber;
    @JsonProperty
    private String email;
    @JsonProperty
    private String nationality;
    @JsonProperty
    private String address;
    @JsonProperty
    private String ssn;
    @JsonProperty
    private Boolean onboarding;
    @JsonProperty
    private byte[] identification;

    public UpdateUserDTO() {}
}
