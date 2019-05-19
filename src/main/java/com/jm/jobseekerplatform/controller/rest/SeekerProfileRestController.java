package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seekerprofiles")
public class SeekerProfileRestController {
    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private SeekerService seekerService;


    @RequestMapping("/")
    public List<SeekerProfile> getAllSeekerProfiles() {
        return seekerProfileService.getAll();
    }

    @RequestMapping("/{seekerProfileId}")
    public SeekerProfile getSeekerProfileById(@PathVariable Long seekerProfileId) {
        return seekerProfileService.getById(seekerProfileId);
    }

    @GetMapping(value = "/email/{seekerEmail}")
    public String getSeekerProfileNameByEmail(@PathVariable String seekerEmail) {
        Seeker seeker = (Seeker) seekerService.findByEmail(seekerEmail);
        if (seeker.getSeekerProfile() == null) {
            return seekerEmail;
        }
        return seeker.getSeekerProfile().getName() + " " + seeker.getSeekerProfile().getSurname();
    }
}


