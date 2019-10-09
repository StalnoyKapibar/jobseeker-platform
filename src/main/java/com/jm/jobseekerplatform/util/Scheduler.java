package com.jm.jobseekerplatform.util;

import com.jm.jobseekerplatform.service.impl.MeetingService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@PropertySource("classpath:config/scheduler.properties")
@Component("scheduler")
public class Scheduler {

    @Autowired
    VacancyService vacancyService;

    @Autowired
    EmployerProfileService employerProfileService;

    @Autowired
    MeetingService meetingService;

    @Scheduled(cron = "${scheduler.properties.deleteExpiryVacancies.cron}")
    public void deleteExpiryVacancies() {
        vacancyService.deletePermanentBlockVacancies();
        vacancyService.deleteExpiryBlockVacancies();
    }

    @Scheduled(cron = "${scheduler.properties.deleteExpiryEmployerProfiles.cron}")
    public void deleteExpiryEmployerProfiles() {
        employerProfileService.deletePermanentBlockEmployerProfiles();
        employerProfileService.deleteExpiryBlockEmployerProfiles();
    }

    @Scheduled(cron = "${scheduler.properties.updateMeetingStatus.cron}")
    public void updateMeetingStatus() {
        meetingService.updateMeetingsOnPassing();
    }
}
