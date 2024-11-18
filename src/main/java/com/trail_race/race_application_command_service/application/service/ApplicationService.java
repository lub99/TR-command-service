package com.trail_race.race_application_command_service.application.service;

import com.trail_race.race_application_command_service.application.dto.ApplicationRequest;

public interface ApplicationService {

    void createApplication(ApplicationRequest applicationRequest);

    void patchApplication(ApplicationRequest applicationRequest, String id);

    void deleteApplication(String id);
}
