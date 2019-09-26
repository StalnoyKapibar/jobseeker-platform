package com.jm.jobseekerplatform.util;

import com.jm.jobseekerplatform.service.impl.MeetingService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
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

    @Autowired
    UserService userService;

    @Scheduled(cron = "${scheduler.deleteExpiryVacancies.cron}")
    public void deleteExpiryVacancies() {
        vacancyService.deletePermanentBlockVacancies();
        vacancyService.deleteExpiryBlockVacancies();
    }

    @Scheduled(cron = "${scheduler.deletePermanentBlockUsers.cron}")
    public void deleteExpiryEmployerProfiles() {
        userService.deletePermanentBlockUsers();
    }

    @Scheduled(cron = "${scheduler.unblockBlockedUsersWithExpiryBanDate.cron}")
    public void unblockBlockedUsersWithExpiryBanDate() {
        userService.unblockBlockedUsersWithExpiryBanDate();
    }

    @Scheduled(cron = "${scheduler.updateMeetingStatus.cron}")
    public void updateMeetingStatus() {
        meetingService.updateMeetingsOnPassing();
    }
}
