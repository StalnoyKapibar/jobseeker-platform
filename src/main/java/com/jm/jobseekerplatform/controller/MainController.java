package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

@Controller
public class MainController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public String mainPage() {
        return "index";
    }

    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        model.addAttribute("eprofile", employerProfile);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));
        return "employer";
    }

    @RequestMapping("/seeker/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("sprofile", seekerProfile);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerProfile.getPhoto()));
        return "seeker";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }
}
