package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EmployerController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        boolean isOwner = false;
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);

        model.addAttribute("employerProfile", employerProfile);

        Set<Vacancy> vacancies = vacancyService.getAllByEmployerProfileId(employerProfile.getId());
        model.addAttribute("vacancies", vacancies);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> (grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                EmployerUser employerUser = employerUserService.getById(userId);
                isOwner = employerUser.getProfile().getId().equals(employerProfileId);
            }
            if (roles.contains("ROLE_SEEKER") | roles.contains("ROLE_ADMIN")) {
                if (roles.contains("ROLE_SEEKER")) {
                    boolean isContain = false;
                    SeekerUser seekerUser = seekerUserService.getById(userId);
                    if (employerProfile.getReviews().stream().anyMatch(reviews1 -> Objects.equals(reviews1.getSeekerProfile().getId(), seekerUser.getProfile().getId()))) {
                        isContain = true;
                    }
                    model.addAttribute("seekerProfileId", seekerUser.getProfile().getId());
                    model.addAttribute("isContain", isContain);
                }
            }
        }

        if (!employerProfile.getReviews().isEmpty()) {
            model.addAttribute("employerProfileReviews", employerProfile.getReviews());
            model.addAttribute("reviewStatus", true);
            model.addAttribute("averageRating", employerProfile.getAverageRating());
        } else {
            model.addAttribute("reviewStatus", false);
        }

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "employer";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping("/employer/get_news/{employerProfileId}")
    public String getEmployerProfileNews(@PathVariable Long employerProfileId, Model model) {
        model.addAttribute("employerProfileId", employerProfileId);
        return "employer_all_news";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping("/employer/chats/{employerProfileId}")
    public String EmployerPageChatsMy(@PathVariable Long employerProfileId, Model model) {
        model.addAttribute("employerProfileId", employerProfileId);
        model.addAttribute("chats", chatWithTopicService.getAllChatsByMemberProfileId(employerProfileId));
        return "employer_chats_my";
    }
}
