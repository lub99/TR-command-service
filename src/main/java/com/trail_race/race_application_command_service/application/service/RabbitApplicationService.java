package com.trail_race.race_application_command_service.application.service;

import com.trail_race.race_application_command_service.application.dto.ApplicationRequest;
import com.trail_race.race_application_command_service.exception.dao.BadRequestException;
import com.trail_race.race_application_command_service.rabbit.RabbitMqSender;
import com.trail_race.race_application_command_service.rabbit.data.CommandMessage;
import com.trail_race.race_application_command_service.rabbit.data.CommandType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitApplicationService implements ApplicationService {

    private final RabbitMqSender rabbitMqSender;

    public void createApplication(ApplicationRequest applicationRequest) {
        String id = UUID.randomUUID().toString();
        validateCreateApplication(applicationRequest);
        CommandMessage commandMessage = getCommandMessage(applicationRequest, id, CommandType.CREATE);
        rabbitMqSender.sendCommand(commandMessage);
    }

    public void patchApplication(ApplicationRequest applicationRequest, String id) {
        CommandMessage commandMessage = getCommandMessage(applicationRequest, id, CommandType.PATCH);
        rabbitMqSender.sendCommand(commandMessage);
    }

    public void deleteApplication(String id) {
        CommandMessage deleteCommandMessage = CommandMessage.builder()
                .commandType(CommandType.DELETE)
                .id(id)
                .build();
        rabbitMqSender.sendCommand(deleteCommandMessage);
    }

    private CommandMessage getCommandMessage(ApplicationRequest applicationRequest, String id,
                                             CommandType commandType) {
        return CommandMessage.builder()
                .id(id)
                .commandType(commandType)
                .firstName(applicationRequest.getFirstName())
                .lastName(applicationRequest.getLastName())
                .club(applicationRequest.getClub())
                .distance(applicationRequest.getDistance())
                .build();
    }

    private void validateCreateApplication(ApplicationRequest applicationRequest) {
        if (applicationRequest.getFirstName() == null) {
            throw new BadRequestException("FirstName is null");
        }
        if (applicationRequest.getLastName() == null) {
            throw new BadRequestException("LastName is null");
        }
        if (applicationRequest.getDistance() == null) {
            throw new BadRequestException("Distance is null");
        }
    }
}
