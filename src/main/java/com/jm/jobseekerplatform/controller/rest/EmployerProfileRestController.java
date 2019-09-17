package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @RequestMapping(value = "/companies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<EmployerProfile>> getCompanies() {
        return ResponseEntity.ok(employerProfileService.getAll());
    }
}
