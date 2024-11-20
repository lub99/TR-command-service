package com.trail_race.race_application_command_service.exception.dao;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
