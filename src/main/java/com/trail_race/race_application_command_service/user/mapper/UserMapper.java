package com.trail_race.race_application_command_service.user.mapper;

import com.trail_race.race_application_command_service.user.dto.UserResponse;
import com.trail_race.race_application_command_service.user.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper {

    public abstract UserResponse modelToResponse(User user);
}
