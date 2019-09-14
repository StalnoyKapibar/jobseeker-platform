package com.jm.jobseekerplatform.aop;

import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.SeekerHistoryService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class SeekerJournal {

    private GrantedAuthority roleSeeker = new SimpleGrantedAuthority("ROLE_SEEKER");

    @Autowired
    private SeekerHistoryService seekerHistoryService;

    @Autowired
    private VacancyService vacancyService;

    @Pointcut( "execution(public * com.jm.jobseekerplatform.controller.MainController.viewVacancy(..))")
    public void monitor(){}

    @Before("monitor() && args(vacancyId,..)")
    public void getHistory(JoinPoint joinPoint, Long vacancyId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(roleSeeker)) {
            SeekerProfile seeker = (SeekerProfile)((User)auth.getPrincipal()).getProfile();
            Vacancy vacancy = vacancyService.getById(vacancyId);
            LocalDateTime date = LocalDateTime.now();
            seekerHistoryService.add(new SeekerVacancyRecord(date, seeker, vacancy));
        }
    }
}
