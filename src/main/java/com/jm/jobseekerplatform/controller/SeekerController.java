package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ResumeService resumeService;
  
    @Autowired
    private EmployerProfileService employerProfileService;
  
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping("/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model, Authentication authentication) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Set<Tag> tags = seekerProfile.getTags();
        List<Tag> notSelectedTags = tagService.getAll();
        notSelectedTags.removeAll(tags);
        model.addAttribute("notSelectedTags", notSelectedTags);
        model.addAttribute("seekerProfile", seekerProfile);
        model.addAttribute("photoimg", seekerProfile.getEncoderPhoto());
        Long loggedProfileId = ((User) authentication.getPrincipal()).getProfile().getId();
        model.addAttribute("isProfileOwner", seekerProfileId.equals(loggedProfileId));
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("seekerUser", seekerUserService.getByProfileId(seekerProfileId));
        }
        return "seeker";
    }

    @GetMapping("/meetings/{seekerProfileId}")
    public String seekerMeetingsPage(@PathVariable Long seekerProfileId, Model model, Authentication authentication) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Long id = ((User) authentication.getPrincipal()).getProfile().getId();
        model.addAttribute("isOwner", seekerProfileId.equals(id));
        model.addAttribute("seekerProfile", seekerProfile);
        return "meetings";
    }

    @GetMapping("/vacancies/{seekerProfileId}")
    public String seekerFavoriteVacancies(@PathVariable Long seekerProfileId, Model model) {
        Set<Vacancy> favoriteVacancy = seekerProfileService.getById(seekerProfileId).getFavoriteVacancy();
        model.addAttribute("favoriteVacancy", favoriteVacancy);
        return "seeker_favorite_vacancies";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @GetMapping("/get_subscriptions/{seekerProfileId}")
    public String getSeekerSubscriptions(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("seekerProfileSubscriptions", seekerProfile.getSubscriptions());
        model.addAttribute("seekerProfileId", seekerProfileId);
        return "seeker_subscriptions";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @GetMapping("/get_subscription_news/{seekerProfileId}")
    public String getSeekerSubscriptionNews(@PathVariable Long seekerProfileId, Model model) {
        model.addAttribute("seekerProfileId", seekerProfileId);
        return "seeker_subscription_news";
    }

    @GetMapping("/resumes/{seekerProfileId}")
    public String seekerResumesPage(@PathVariable Long seekerProfileId, Model model, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYER"))) {
            SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
            model.addAttribute("seekerProfileId", ((User) authentication.getPrincipal()).getProfile().getId());
            model.addAttribute("resumesList", seekerProfile.getResumes());
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
            return "resumes";
        } else {
            SeekerProfile seekerProfile = seekerProfileService.getById(((User) authentication
                    .getPrincipal())
                    .getProfile()
                    .getId());
            model.addAttribute("seekerProfileId", ((User) authentication.getPrincipal()).getProfile().getId());
            model.addAttribute("resumesList", seekerProfile.getResumes());
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
            return "resumes";
        }
    }

    @GetMapping("/chats/{seekerProfileId}")
    public String EmployerPageChatsMy(@PathVariable Long seekerProfileId, Model model) {
        model.addAttribute("seekerProfileId", seekerProfileId);
        model.addAttribute("chats", chatWithTopicService.getAllChatsByMemberProfileId(seekerProfileId));
        return "seeker_chats";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping("/resumes/{seekerProfileId}/{resumeId}")
    public String getSingleResume(@PathVariable Long resumeId,
                                  @PathVariable Long seekerProfileId,
                                  Model model, Authentication authentication) {
        if (((User) authentication.getPrincipal()).getProfile().getId() == seekerProfileId &&
                seekerProfileService.getById(seekerProfileId).getId() ==
                        resumeService.getById(resumeId).getCreatorProfile().getId()) {
            model.addAttribute("resumeID", resumeId);
            model.addAttribute("userRole", ((User) authentication.getPrincipal()).getAuthority());
        }
        return "resume/single_resume";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping("/resumes/new")
    public String addNewResume(Model model) {
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "resume/new_resume";
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping(value = "/resumes/edit/{resumeId}", method = RequestMethod.GET)
    public String editResume(@PathVariable("resumeId") Long resumeId, Authentication authentication, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(((User) authentication.getPrincipal()).getId());
        if (resumeService.getById(resumeId).getCreatorProfile().getId() == seekerProfile.getId()) {
            model.addAttribute("oldResume", resumeService.getById(resumeId));
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        }
        return "resume/edit_resume";
    }

    @RequestMapping("/update/{seekerProfileId}")
    public String updateSeekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("seekerProfile", seekerProfile);
        model.addAttribute("photoimg", seekerProfile.getEncoderPhoto());
        return "update_seeker_profile";
    }

    @GetMapping("/companies")
    public String getCompaniesList(Model model) {
        List<EmployerProfile> employerprofiles = employerProfileService.getAll();
        model.addAttribute("companiesList", employerprofiles);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "companies";
    }
}
