package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping("/")
    public String mainPage() {
        return "index";
    }

    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        model.addAttribute("eprofile", employerProfile);
        return "employer";
    }
}
