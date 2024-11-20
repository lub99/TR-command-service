package com.trail_race.race_application_command_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
final class Responses {

    private Responses() {
    }

    static ResponseEntity<ErrorResponse> getAndLogDebug(Exception e, String message, HttpServletRequest req,
                                                        HttpStatus httpStatus) {
        log.debug(req.getRequestURI() + " : " + message, e);
        return getErrorResponse(e, message, req, httpStatus);
    }

    static ResponseEntity<ErrorResponse> getAndLogError(Exception e, String message, HttpServletRequest req,
                                                        HttpStatus httpStatus) {

        log.error(message, e);
        return getErrorResponse(e, message, req, httpStatus);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponse(Exception e, String message, HttpServletRequest req,
                                                                  HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(ErrorResponse.builder()
                .message(message)
                .exception(e.getClass().getName())
                .status(httpStatus.value())
                .stacktrace(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                        .collect(Collectors.joining(";\n")))
                .path(req.getContextPath() + "/" + req.getServletPath())
                .params(req.getParameterMap())
                .headers(getHeaders(req))
                .build());
    }

    private static Map<String, Object> getHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .filter(s -> !"authorization".equals(s))
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h))
                ));
    }
}
