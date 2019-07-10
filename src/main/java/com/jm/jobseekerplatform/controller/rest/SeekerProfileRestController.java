package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seekerprofiles")
public class SeekerProfileRestController {

    @Autowired
    private SeekerProfileService seekerProfileService;
    
    @RequestMapping("/")
    public List<SeekerProfile> getAllSeekerProfiles() {
        return seekerProfileService.getAll();
    }

    @RequestMapping("/{seekerProfileId}")
    public SeekerProfile getSeekerProfileById(@PathVariable Long seekerProfileId) {
        return seekerProfileService.getById(seekerProfileId);
    }
}


