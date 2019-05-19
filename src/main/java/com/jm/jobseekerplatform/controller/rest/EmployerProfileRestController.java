package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employerprofiles")
public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    EmployerService employerService;

    @RequestMapping("/")
    public List<EmployerProfile> getAllEmployerProfiles() {
        List<EmployerProfile> employerprofiles = employerProfileService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{employerProfileId}")
    public EmployerProfile getEmployerProfileById(@PathVariable Long employerProfileId) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        return employerProfile;
    }

    @GetMapping(value = "/email/{employerEmail}")
    public String getEmployerProfileNameByEmail(@PathVariable String employerEmail) {
        Employer employer = (Employer) employerService.findByEmail(employerEmail);
        if (employer.getEmployerProfile() == null) {
            return employerEmail;
        }
        return employer.getEmployerProfile().getCompanyName();
    }
}
