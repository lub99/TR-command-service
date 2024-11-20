package com.trail_race.race_application_command_service.exception.dao;


public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
