package com.trail_race.race_application_command_service.user.service;

import com.trail_race.race_application_command_service.exception.dao.NotFoundException;
import com.trail_race.race_application_command_service.user.dto.UserResponse;
import com.trail_race.race_application_command_service.user.mapper.UserMapper;
import com.trail_race.race_application_command_service.user.model.User;
import com.trail_race.race_application_command_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
        return userMapper.modelToResponse(user);
    }
}
