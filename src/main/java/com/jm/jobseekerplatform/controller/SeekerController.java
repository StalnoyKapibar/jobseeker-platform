package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;

@Controller
public class SeekerController {

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/seeker/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        ProfileSeeker profileSeeker = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("sprofile", profileSeeker);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(profileSeeker.getPhoto()));
        return "seeker";
    }
}
