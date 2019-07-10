package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employerprofiles")
public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping("/")
    public List<ProfileEmployer> getAllEmployerProfiles() {
        List<ProfileEmployer> employerprofiles = employerProfileService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{employerProfileId:\\d+}")
    public ProfileEmployer getEmployerProfileById(@PathVariable Long employerProfileId){
        ProfileEmployer profileEmployer = employerProfileService.getById(employerProfileId);
        return profileEmployer;
    }

    @RequestMapping(value = "/block/{vacancyId:\\d+}", method = RequestMethod.POST)
    public void blockEmployerProfile(@PathVariable("vacancyId") Long id, @RequestBody int periodInDays) {
        ProfileEmployer profileEmployer = employerProfileService.getById(id);
        if (periodInDays == 0){
            employerProfileService.blockPermanently(profileEmployer);
        }
        if (periodInDays > 0 && periodInDays < 15){
            employerProfileService.blockTemporary(profileEmployer, periodInDays);
        }
    }
}
