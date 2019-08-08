package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.service.impl.SeekerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/seeker_vacancy_record/")
public class SeekerVacancyRecordRestController {

    @Autowired
    SeekerHistoryService seekerHistoryService;

    @RolesAllowed("ROLE_SEEKER")
    @RequestMapping(value = "{seekerId}", method = RequestMethod.GET)
    public List<Map<String, String>> getAll(@PathVariable("seekerId") Long seekerId) {
        return seekerHistoryService.getViewedVacanciesBySeeker(seekerId);
    }
}
