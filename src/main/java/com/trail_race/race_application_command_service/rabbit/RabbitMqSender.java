package com.trail_race.race_application_command_service.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trail_race.race_application_command_service.exception.dao.CommandSerializationException;
import com.trail_race.race_application_command_service.rabbit.data.CommandMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendCommand(CommandMessage commandMessage) {
        try {
            log.info("Sending command:" + commandMessage);
            byte[] raw = objectMapper.writer().writeValueAsString(commandMessage).getBytes(StandardCharsets.UTF_8);
            rabbitTemplate.send("command", new Message(raw));
        } catch (JsonProcessingException e) {
            String message = "Error serializing command. Id:" + commandMessage.getId() + ", commandType:"
                    + commandMessage.getCommandType();
            throw new CommandSerializationException(message, e);
        }
    }
}
