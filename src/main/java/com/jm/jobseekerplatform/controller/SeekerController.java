package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.util.Base64;
import java.util.Set;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    @Autowired
    private UserService userService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private NewsService newsService;

    @RequestMapping("/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model, Authentication authentication) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("seekerProfile", seekerProfile);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerProfile.getPhoto()));
        return "seeker";
    }

    @RequestMapping("/meetings/{seekerProfileId}")
    public String seekerMeetingsPage(@PathVariable Long seekerProfileId, Model model, Authentication authentication) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        boolean isOwner = false;
        if (authentication != null) {
            Long id = ((User) authentication.getPrincipal()).getId();
            if(seekerProfileId.equals(id)){
                isOwner=true;
            }
        }
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("seekerProfile", seekerProfile);
        return "meetings";
    }

    @RequestMapping("/vacancies/{seekerProfileId}")
    public String seekerFavoriteVacancies(@PathVariable Long seekerProfileId, Model model) {
        Set<Vacancy> favoriteVacancy = seekerProfileService.getById(seekerProfileId).getFavoriteVacancy();
        model.addAttribute("favoriteVacancy", favoriteVacancy);
        return "seeker_favorite_vacancies";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping("/get_subscriptions/{seekerProfileId}")
    public String getSeekerSubscriptions(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("seekerProfileSubscriptions", seekerProfile.getSubscriptions());
        model.addAttribute("seekerProfileId", seekerProfileId);
        return "seeker_subscriptions";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping("/get_subscription_news/{seekerProfileId}")
    public String getSeekerSubscriptionNews(@PathVariable Long seekerProfileId, Model model) {
        model.addAttribute("seekerProfileId", seekerProfileId);
        return "seeker_subscription_news";
    }
}
