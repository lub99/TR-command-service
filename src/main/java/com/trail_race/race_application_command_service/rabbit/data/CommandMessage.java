package com.trail_race.race_application_command_service.rabbit.data;

import com.trail_race.race_application_command_service.application.model.Distance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandMessage {
    private CommandType commandType;
    private String id;
    private String firstName;
    private String lastName;
    private String club;
    private Distance distance;
}
