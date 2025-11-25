package com.example.abc_onboarding.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Database primary key")
    private Long id;

    @Column(name = "account_id", unique = true)
    @Schema(description = "Unique account ID (8-char alphanumeric, separate from the db id)")
    private String accountId;

    @NotBlank(message = "First Name is a required field.")
    @Schema(description = "First name")
    private String firstName;

    @NotBlank(message = "Last Name is a required field.")
    @Schema(description = "Last name")
    private String lastName;

    @NotBlank(message = "Gender is a required field.")
    @Schema(description = "Gender")
    private String gender;

    @NotBlank(message = "Date of Birth is a required field.")
    @Schema(description = "Date of birth (YYYY-MM-DD)")
    private String dateOfBirth;

    @Schema(description = "Phone number")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    @Schema(description = "Email address")
    private String email;

    @NotBlank(message = "Nationality is a required field.")
    @Schema(description = "Nationality/Citizenship")
    private String nationality;

    @NotBlank(message = "Address is a required field.")
    @Schema(description = "Address")
    private String address;

    @NotBlank(message = "SSN is a required field.")
    @Schema(description = "Social Security Number")
    private String ssn;

    @Schema(description = "TRUE for users who are not fully onboarded and confirmed/accepted.")
    private boolean onboarding;

    @Lob
    @NotNull(message = "Identification file is required.")
    @Schema(description = "Uploaded passport/photo ID (binary data)")
    private byte[] identification;

    public User() {}
}
