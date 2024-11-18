package com.trail_race.race_application_command_service.application.dto;

import com.trail_race.race_application_command_service.application.model.Distance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private String firstName;
    private String lastName;
    private String club;
    private Distance distance;
}
