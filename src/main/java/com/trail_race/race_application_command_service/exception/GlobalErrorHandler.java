package com.trail_race.race_application_command_service.exception;


import com.trail_race.race_application_command_service.exception.dao.BadRequestException;
import com.trail_race.race_application_command_service.exception.dao.CommandSerializationException;
import com.trail_race.race_application_command_service.exception.dao.ForbiddenException;
import com.trail_race.race_application_command_service.exception.dao.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorHandler {

    @ResponseBody  // 400
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BadRequestException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(HttpServletRequest req, Exception e) {
        if (e instanceof MethodArgumentNotValidException exc) {
            Map<String, String> errors = new HashMap<>();
            exc.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            return Responses.getAndLogDebug(e, errors.toString(), req, HttpStatus.BAD_REQUEST);
        }

        return Responses.getAndLogDebug(e, e.getMessage(), req,  HttpStatus.BAD_REQUEST);
    }

    @ResponseBody // 403
    @ExceptionHandler({
            AuthenticationException.class,
            ForbiddenException.class,
            BadCredentialsException.class,
            BadJwtException.class
    })
    public ResponseEntity<ErrorResponse> handleForbiddenExceptions(HttpServletRequest req, Exception e) {
        return Responses.getAndLogDebug(e, e.getMessage(), req, HttpStatus.FORBIDDEN);
    }

    @ResponseBody // 404
    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest req, Exception e) {
        return Responses.getAndLogDebug(e, e.getMessage(), req, HttpStatus.NOT_FOUND);
    }

    @ResponseBody  // 500
    @ExceptionHandler({
            CommandSerializationException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleGenericExceptions(HttpServletRequest req, Exception e) {
        return Responses.getAndLogError(e, e.getMessage(), req, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
