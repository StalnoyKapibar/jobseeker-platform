package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Set;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("sprofile", seekerProfile);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerProfile.getPhoto()));
        return "seeker";
    }

    @RequestMapping("/vacancies/{seekerProfileId}")
    public String seekerFavoriteVacancies(@PathVariable Long seekerProfileId, Model model){
        Set<Vacancy> favoriteVacancy = seekerProfileService.getById(seekerProfileId).getFavoriteVacancy();
        model.addAttribute("favoriteVacancy", favoriteVacancy);
        return "seeker_favorite_vacancies";
    }
}
