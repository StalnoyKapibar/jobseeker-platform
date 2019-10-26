package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.UserTimerDTO;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.SubscriptionService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.security.RolesAllowed;
import java.util.*;

@RestController
@RequestMapping("/api/seekerprofiles")
public class SeekerProfileRestController {

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TagService tagService;

    @Autowired
    EmployerProfileRestController employerProfileRestController;

    @RequestMapping("/")
    public List<SeekerProfile> getAllSeekerProfiles() {
        return seekerProfileService.getAll();
    }

    @RequestMapping("/{seekerProfileId}")
    public SeekerProfile getSeekerProfileById(@PathVariable Long seekerProfileId) {
        return seekerProfileService.getById(seekerProfileId);
    }

    @PostMapping(value = "/outFavoriteVacancy")
    public ResponseEntity outFavoriteVacancy(@RequestParam Long vacancyId, @RequestParam Long seekerProfileId) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        seekerProfile.getFavoriteVacancy().remove(vacancy);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/inFavoriteVacancy")
    public ResponseEntity inFavoriteVacancy(@RequestParam Long vacancyId, @RequestParam Long seekerProfileId) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        seekerProfile.getFavoriteVacancy().add(vacancy);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/unSubscribe")
    public ResponseEntity unSubscribeCompany(@RequestParam Long vacancyId, @RequestParam Long seekerProfileId) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        EmployerProfile employerProfile = employerProfileService.getById(((Profile) vacancy.getCreatorProfile()).getId());
        Subscription subscription = subscriptionService.findBySeekerAndEmployer(seekerProfile, employerProfile);
        subscriptionService.deleteSubscription(subscription);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/toSubscribe")
    public ResponseEntity toSubscribeCompany(@RequestParam Long vacancyId, @RequestParam Long seekerProfileId,
                                             @RequestParam Set<String> tags) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        EmployerProfile employerProfile = employerProfileService.getById(((Profile) vacancy.getCreatorProfile()).getId());
        Set<Tag> tagSet = tagService.getTagsByStringNames(tags);
        Subscription subscription = new Subscription(employerProfile, seekerProfile, tagSet);
        seekerProfile.getSubscriptions().add(subscription);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/deleteSubscription")
    public ResponseEntity deleteSubscription(@RequestParam Long employerProfileId, @RequestParam Long seekerProfileId) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        Subscription subscription = subscriptionService.findBySeekerAndEmployer(seekerProfile, employerProfile);
        subscriptionService.deleteSubscription(subscription);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed("ROLE_SEEKER")
    @PostMapping(value = "/updateUserTags")
    public ResponseEntity updateUserTags(@RequestBody String[] updatedTags) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        SeekerUser seekerUser = (SeekerUser) authentication.getPrincipal();
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerUser.getProfile().getId());

        Set<String> tagsSet = new HashSet<>(Arrays.asList(updatedTags));
        Set<Tag> tagsByStringNames = tagService.getTagsByStringNames(tagsSet);
        Set<Tag> seekerProfileTags = seekerProfile.getTags();
        seekerProfileTags.addAll(tagsByStringNames);
        seekerProfile.setTags(seekerProfileTags);

        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed("ROLE_SEEKER")
    @PostMapping(value = "/removeTag")
    public ResponseEntity removeTag(@RequestBody String tag) {
        String t = tag.replaceAll("[^A-Za-z0-9/ ]", "").trim();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        SeekerUser seekerUser = (SeekerUser) authentication.getPrincipal();
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerUser.getProfile().getId());
        Set<Tag> seekerProfileTags = seekerProfile.getTags();
        seekerProfileTags.removeIf(next -> next.getName().equals(t));
        seekerProfile.setTags(seekerProfileTags);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/update")
    public SeekerProfile updateSeekerProfile(@RequestBody SeekerProfile seekerProfile) {
        SeekerProfile updatedProfile = seekerProfileService.getById(seekerProfile.getId());
        updatedProfile.setName(seekerProfile.getName());
        updatedProfile.setSurname(seekerProfile.getSurname());
        updatedProfile.setPatronymic(seekerProfile.getPatronymic());
        updatedProfile.setDescription(seekerProfile.getDescription());

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        SeekerUser seekerUser = (SeekerUser) authentication.getPrincipal();
        SeekerProfile seekerProfile2 = seekerProfileService.getById(seekerUser.getProfile().getId());
        Set<Tag> seekerProfileTags = seekerProfile2.getTags();

        updatedProfile.setTags(seekerProfileTags);
        seekerProfileService.update(updatedProfile);
        return updatedProfile;
    }

    @PostMapping(value = "/update_image")
    public String updateImage(@RequestParam long id, @RequestParam MultipartFile image) {
        seekerProfileService.updatePhoto(id, image);
        return seekerProfileService.getById(id).getEncoderPhoto();
    }

    @GetMapping("/seeker_timer")
    public List<UserTimerDTO> seekerTimer() {
        List<SeekerProfile> profiles = seekerProfileService.getProfilesExpNotNull();
        List<UserTimerDTO> userTimerDTO = new ArrayList<>();

        for (SeekerProfile sp : profiles) {
            User user = seekerProfileService.getUserBySeekerProfile(sp);
            Date expireDate = sp.getExpiryBlock();
            userTimerDTO.add(employerProfileRestController.createUserTimerDTO(expireDate, user));
        }
        return userTimerDTO;
    }

}
