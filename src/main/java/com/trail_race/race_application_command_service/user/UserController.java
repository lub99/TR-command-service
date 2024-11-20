package com.trail_race.race_application_command_service.user;

import com.trail_race.race_application_command_service.user.dto.UserResponse;
import com.trail_race.race_application_command_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/logged-in")
    public ResponseEntity<UserResponse> getLoggedInUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getByEmail(authentication.getName()));
    }
}
