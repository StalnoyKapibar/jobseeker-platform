package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.SubscriptionService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.tokens.VerificationTokenService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;



@Controller
public class MainController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private SeekerUserService seekerUserService;
    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmployerProfileService employerProfileService;

    private GrantedAuthority roleSeeker = new SimpleGrantedAuthority("ROLE_SEEKER");
    private GrantedAuthority roleEmployer = new SimpleGrantedAuthority("ROLE_EMPLOYER");

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @RequestMapping("/")
    public String mainPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("vacMess", "Доступные вакансии:");
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                try {
                    SeekerUser seekerUser = (SeekerUser) authentication.getPrincipal();
                    SeekerProfile profile = seekerUser.getProfile();
                    model.addAttribute("favoriteVacancies", profile.getFavoriteVacancy());
                    model.addAttribute("seekerProfileId", profile.getId());
                    model.addAttribute("googleMapsApiKey", googleMapsApiKey);
                    model.addAttribute("seekerAuthority", seekerUser.getAuthority());
                    model.addAttribute("vacMess", "Вакансии с учетом Вашего опыта:");
                } catch (NullPointerException e) {
                    model.addAttribute("googleMapsApiKey", googleMapsApiKey);
                    model.addAttribute("vacMess", "Доступные вакансии: " +
                            "(Создайте свой профиль, чтобы увидеть вакансии с учетом Вашего опыта)");
                }
            } else {
                model.addAttribute("googleMapsApiKey", googleMapsApiKey);
            }
            if (authentication.getAuthorities().contains(roleEmployer)) {
                Profile profile = ((User) authentication.getPrincipal()).getProfile();
                model.addAttribute("employerProfileId", profile.getId());
            }
        }
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "logout", required = false) String logout,
                        HttpServletRequest request,
                        Model model) {
        if (logout != null) {
            model.addAttribute("logout", logout);
        } else {
            if (request.getSession().getAttribute("error") != null) {
                model.addAttribute("error", request.getSession().getAttribute("error"));
                return "login";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String reg(@RequestParam(value = "email", required = false) String email, Model model) {
        model.addAttribute("email", email);
        return "registration";
    }

    @RequestMapping(value = "/confirm_reg/{token}", method = RequestMethod.GET)
    public String confirmRegistration(@PathVariable String token, Model model) {

        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if (verificationToken != null) {
            boolean complete = verificationTokenService.tokenIsNonExpired(verificationToken);
            model.addAttribute("complete", complete);
            if (complete) {
                verificationTokenService.completeRegistration(verificationToken);
            }
        } else {
            model.addAttribute("complete", false);
        }
        return "confirm_reg";

    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String filterProfilePage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Long id = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority ->
                    ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                EmployerUser employerUser = employerUserService.getById(id);
                return "redirect:/employer/" + employerUser.getProfile().getId();
            } else if (roles.contains("ROLE_SEEKER")) {
                SeekerUser seekerUser = seekerUserService.getById(id);
                return "redirect:/seeker/" + seekerUser.getProfile().getId();
            } else if (roles.contains("ROLE_ADMIN")) {
                return "redirect:/admin";
            }
        }
        return "index";
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/new_vacancy", method = RequestMethod.GET)
    public String new_vacancyPage(Model model,  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        model.addAttribute("employerProfileId", user.getProfile().getId());
        return "/vacancy/new_vacancy";
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/edit_vacancy/{vacancyId}", method = RequestMethod.GET)
    public String edit_vacancyPage(@PathVariable("vacancyId") Long vacancyId, Authentication authentication,
                                   Model model) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        EmployerProfile employerProfile = employerProfileService.getById(userId);
        model.addAttribute("employer", employerProfile);
        String employerName = ((User) authentication.getPrincipal()).getUsername();
        if (vacancyService.getById(vacancyId).getCreatorProfile().getId() == employerProfile.getId()) {
            model.addAttribute("vacancy", vacancyService.getById(vacancyId));
        }
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "/vacancy/edit_vacancy";
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/add_news", method = RequestMethod.GET)
    public String addNewsPage(Model model, Authentication authentication) {
        model.addAttribute("employerProfileId", ((User) authentication.getPrincipal()).getProfile().getId());
        return "add_news";
    }

    @RequestMapping(value = "/vacancy/{vacancyId}", method = RequestMethod.GET)
    public String viewVacancy(@PathVariable Long vacancyId, Model model, Authentication authentication) {
        Vacancy vacancy = vacancyService.getById(vacancyId);
        if (authentication != null) {
            boolean isContain;
            boolean isSubscribe;
            boolean hasResponded;
            Long id = ((User) authentication.getPrincipal()).getId();
            Profile profile = userService.getById(id).getProfile();
            if (profile instanceof SeekerProfile) {
                Subscription subscription = subscriptionService.findBySeekerAndEmployer((SeekerProfile) profile,
                        vacancy.getCreatorProfile());
                isContain = ((SeekerProfile) profile).getFavoriteVacancy().contains(vacancy);
                isSubscribe = ((SeekerProfile) profile).getSubscriptions().contains(subscription);
                hasResponded = vacancy.getMeetings()
                        .stream().filter(meeting ->
                                meeting.getSeekerProfile().getId().equals(id)).findFirst().isPresent();
                model.addAttribute("isContain", isContain);
                model.addAttribute("isSubscribe", isSubscribe);
                model.addAttribute("seekerProfileId", profile.getId());
                model.addAttribute("hasResponded", hasResponded);
            }
            model.addAttribute("profileId", profile.getId());
        }
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        model.addAttribute("vacancyFromServer", vacancy);
        model.addAttribute("EmployerProfileFromServer", vacancy.getCreatorProfile());
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(vacancy.getCreatorProfile().getLogo()));
        return "/vacancy/vacancy";
    }

    @RequestMapping(value = "/recovery", method = RequestMethod.GET)
    public String recoveryPassPage() {
        return "recovery";
    }

    @RequestMapping(value = "/password_reset/{token}", method = RequestMethod.GET)
    public String newPassPage(@PathVariable String token, Model model) {

        User resetPassUser = userService.findUserByTokenValue(token);
        if (resetPassUser != null) {
            model.addAttribute("email", resetPassUser.getEmail());
            model.addAttribute("token", token);
            model.addAttribute("exists", true);
        } else {
            model.addAttribute("exists", false);
        }
        return "password_reset";
    }
}
