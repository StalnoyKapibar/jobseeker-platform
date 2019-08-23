package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.SubscriptionService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @RequestMapping("/")
    public List<SeekerProfile> getAllSeekerProfiles() {
        return seekerProfileService.getAll();
    }

    @RequestMapping("/{seekerProfileId}")
    public SeekerProfile getSeekerProfileById(@PathVariable Long seekerProfileId) {
        return seekerProfileService.getById(seekerProfileId);
    }

    @RequestMapping(value = "/outFavoriteVacancy", method = RequestMethod.POST)
    public ResponseEntity outFavoriteVacancy(@RequestParam("vacancyId") Long vacancyId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        seekerProfile.getFavoriteVacancy().remove(vacancy);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/inFavoriteVacancy", method = RequestMethod.POST)
    public ResponseEntity inFavoriteVacancy(@RequestParam("vacancyId") Long vacancyId,
                                            @RequestParam("seekerProfileId") Long seekerProfileId) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        seekerProfile.getFavoriteVacancy().add(vacancy);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/unSubscribe", method = RequestMethod.POST)
    public ResponseEntity unSubscribeCompany(@RequestParam("vacancyId") Long vacancyId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        EmployerProfile employerProfile =  employerProfileService.getById(((Profile)vacancy.getCreatorProfile()).getId());
        Subscription subscription = subscriptionService.findBySeekerAndEmployer(seekerProfile, employerProfile);
        subscriptionService.deleteSubscription(subscription);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/toSubscribe", method = RequestMethod.POST)
    public ResponseEntity toSubscribeCompany(@RequestParam("vacancyId") Long vacancyId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId,
                                             @RequestParam("tags") Set<String> tags) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        EmployerProfile employerProfile =  employerProfileService.getById(((Profile)vacancy.getCreatorProfile()).getId());
        Set<Tag> tagSet = tagService.getTagsByStringNames(tags);
        Subscription subscription = new Subscription(employerProfile, seekerProfile, tagSet);
        seekerProfile.getSubscriptions().add(subscription);
        seekerProfileService.update(seekerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteSubscription")
    public ResponseEntity deleteSubscription(@RequestParam("employerProfileId") Long employerProfileId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        Subscription subscription = subscriptionService.findBySeekerAndEmployer(seekerProfile, employerProfile);
        subscriptionService.deleteSubscription(subscription);
        return new ResponseEntity(HttpStatus.OK);
    }
}