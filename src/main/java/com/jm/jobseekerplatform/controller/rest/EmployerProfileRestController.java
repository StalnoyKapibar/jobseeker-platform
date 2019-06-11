package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/{employerProfileId:\\d+}")
    public EmployerProfile getEmployerProfileById(@PathVariable Long employerProfileId){
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        return employerProfile;
    }

    @RequestMapping("/vacancy/{vacancyId}")
    public EmployerProfile getEmployerByVacancyId(@PathVariable Long vacancyId) {
        return employerProfileService.getByVacancyId(vacancyId);
    }

    @RequestMapping(value = "/block/{vacancyId:\\d+}", method = RequestMethod.POST)
    public void blockEmployerProfile(@PathVariable("vacancyId") Long id, @RequestBody int periodInDays) {
        EmployerProfile employerProfile = employerProfileService.getById(id);
        if (periodInDays == 0){
            employerProfileService.blockPermanently(employerProfile);
        }
        if (periodInDays > 0 && periodInDays < 15){
            employerProfileService.blockTemporary(employerProfile, periodInDays);
        }
    }
}
