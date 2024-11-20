package com.trail_race.race_application_command_service.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;
}