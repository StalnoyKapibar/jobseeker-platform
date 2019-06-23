package com.jm.jobseekerplatform.aop;

import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.impl.SeekerHistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class SeekerJournal {

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @Autowired
    private SeekerHistoryService seekerHistoryService;

    @Pointcut( "execution(public * com.jm.jobseekerplatform.controller.MainController.viewVacancy(..))")
    public void monitor(){}

    @Before("monitor() && args(vacancyId,..)")
    public void getHistory(JoinPoint joinPoint, Long vacancyId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(roleSeeker)) {
            Long seekerId = ((Seeker)auth.getPrincipal()).getSeekerProfile().getId();
            LocalDateTime date = LocalDateTime.now();
            seekerHistoryService.add(new SeekerVacancyRecord(date, seekerId, vacancyId));
        }
    }
}
