package com.trail_race.race_application_command_service.application.controller;


import com.trail_race.race_application_command_service.application.dto.ApplicationRequest;
import com.trail_race.race_application_command_service.application.service.RabbitApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final RabbitApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Void> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        applicationService.createApplication(applicationRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchApplication(@RequestBody ApplicationRequest applicationRequest,
                                                 @PathVariable String id) {
        applicationService.patchApplication(applicationRequest, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
