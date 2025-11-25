package com.example.abc_onboarding.controller;

import com.example.abc_onboarding.dto.UpdateUserDTO;
import com.example.abc_onboarding.model.User;
import com.example.abc_onboarding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Endpoints for user signup")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user in a multipart request with their legal identification.")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User JSON + identification file"
            )
            @RequestPart("user") User user,

            @Schema(description = "Passport/Photo ID file")
            @RequestPart("identification") MultipartFile identification
    ) {
        try {
            User saved = userService.createUser(user, identification.getBytes());
            return ResponseEntity.ok(saved);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error uploading or reading identification.");
        }
    }

    @Operation(summary = "Fetch user by database ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return (user != null)
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update a User's Onboarding Status and send an Email.")
    @PutMapping("/{id}/onboarding")
    public ResponseEntity<User> updateOnboardingStatus(
            @PathVariable Long id,
            @RequestParam(name = "status") boolean status) {
        try {
            User updated = userService.updateOnboardingStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).build();
        }
    }

    @Operation(summary = "Update user details.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDTO dto) {
        try {
            dto.setId(id);
            User updated = userService.updateUser(dto);
            return ResponseEntity.ok(updated);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).build();
        }
    }

    @Operation(summary = "Delete a user by ID and return Status 204 on success.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).build();
        }
    }


}
