package com.trail_race.race_application_command_service.service;


import com.trail_race.race_application_command_service.application.dto.ApplicationRequest;
import com.trail_race.race_application_command_service.application.model.Distance;
import com.trail_race.race_application_command_service.application.service.RabbitApplicationService;
import com.trail_race.race_application_command_service.exception.dao.BadRequestException;
import com.trail_race.race_application_command_service.rabbit.RabbitMqSender;
import com.trail_race.race_application_command_service.rabbit.data.CommandMessage;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.trail_race.race_application_command_service.rabbit.data.CommandType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private RabbitMqSender rabbitMqSender;

    @InjectMocks
    private RabbitApplicationService rabbitApplicationService;

    @Test
    public void createApplication_correctApplicationRequest_applicationCreated() {
        String firstName = "Mat";
        String lastName = "Lub";
        Distance distance = Distance.MARATHON;
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .distance(distance)
                .build();
        doNothing().when(rabbitMqSender).sendCommand(any(CommandMessage.class));

        rabbitApplicationService.createApplication(applicationRequest);

        verify(rabbitMqSender, times(1)).sendCommand(argThat(commandMessage -> {
            assertNotNull(commandMessage);
            assertEquals(CommandType.CREATE, commandMessage.getCommandType());
            assertEquals(firstName, commandMessage.getFirstName());
            assertEquals(lastName, commandMessage.getLastName());
            assertEquals(distance, commandMessage.getDistance());
            assertNotNull(commandMessage.getId());
            return true;
        }));
    }

    @Test
    public void createApplication_applicationRequestMissingFirstName_throwsBadRequestException() {
        String lastName = "Lub";
        Distance distance = Distance.MARATHON;
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .lastName(lastName)
                .distance(distance)
                .build();

        assertThrows(BadRequestException.class, () -> rabbitApplicationService.createApplication(applicationRequest));
    }

    @Test
    public void patchApplication_correctApplicationRequest_applicationPatched() {
        String firstName = "Mat";
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .firstName(firstName)
                .build();
        String mockId = UUID.randomUUID().toString();
        doNothing().when(rabbitMqSender).sendCommand(any(CommandMessage.class));

        rabbitApplicationService.patchApplication(applicationRequest, mockId);

        verify(rabbitMqSender, times(1)).sendCommand(argThat(commandMessage -> {
            assertNotNull(commandMessage);
            assertEquals(CommandType.PATCH, commandMessage.getCommandType());
            assertEquals(firstName, commandMessage.getFirstName());
            assertEquals(mockId, commandMessage.getId());
            return true;
        }));
    }

    @Test
    public void deleteApplication_correctDelete_applicationDeleted() {
        String mockId = UUID.randomUUID().toString();
        doNothing().when(rabbitMqSender).sendCommand(any(CommandMessage.class));

        rabbitApplicationService.deleteApplication(mockId);

        verify(rabbitMqSender, times(1)).sendCommand(argThat(commandMessage -> {
            assertNotNull(commandMessage);
            assertEquals(CommandType.DELETE, commandMessage.getCommandType());
            assertEquals(mockId, commandMessage.getId());
            return true;
        }));
    }
}
