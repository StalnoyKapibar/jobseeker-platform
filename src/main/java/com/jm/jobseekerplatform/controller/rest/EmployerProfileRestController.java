package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employerprofiles")
public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping("/")
    public List<EmployerProfile> getAllEmployerProfiles() {
        List<EmployerProfile> employerprofiles = employerProfileService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{employerProfileId}")
    public EmployerProfile getUserById(@PathVariable Long employerProfileId){
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        return employerProfile;
    }

    @RequestMapping("/vacancy/{vacancyId}")
    public EmployerProfile getEmployerByVacancyId(@PathVariable Long vacancyId){
        return employerProfileService.getByVacancyId(vacancyId);
    }
}
