package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.ImageService;
import com.jm.jobseekerplatform.service.impl.SubscriptionService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashSet;
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
    private TagService tagService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SubscriptionService subscriptionService;

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

    @PostMapping("/update")
    @ResponseBody
    public SeekerProfile editProfile(@RequestParam(value = "id") long id,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "patronymic", required = false) String patronymic,
                                     @RequestParam(value = "surname", required = false) String surname,
                                     @RequestParam(value = "tags", required = false) List<String> tags,
                                     @RequestParam(value = "description", required = false) String description) {
        SeekerProfile profile = seekerProfileService.getById(id);
        if (name != null && patronymic != null && surname != null) {
            profile.setName(name);
            profile.setPatronymic(patronymic);
            profile.setSurname(surname);
        }
        if (tags != null) {
            profile.setTags(seekerProfileService.getNewTags(tags));
        }
        if (description != null) {
            profile.setDescription(description);
        }
        seekerProfileService.update(profile);
        return profile;
    }

    @RequestMapping(value = "/update_image", method = RequestMethod.POST)
    public String updateImage(@RequestParam(value = "id") long id,
                              @RequestParam(value = "image", required = false) MultipartFile img) throws IOException {

        SeekerProfile profile = seekerProfileService.getById(id);
        if (img != null) {
            profile.setPhoto(imageService.resizePhotoSeeker(ImageIO.read(new ByteArrayInputStream(img.getBytes()))));
            seekerProfileService.update(profile);
        }
        return Base64.getEncoder().encodeToString(profile.getPhoto());
    }



    @RequestMapping(value = "/unSubscribe", method = RequestMethod.POST)
    public ResponseEntity unSubscribeCompany(@RequestParam("vacancyId") Long vacancyId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        EmployerProfile employerProfile = vacancyService.getById(vacancyId).getEmployerProfile();
        Subscription subscription = subscriptionService.findBySeekerAndEmployer(seekerProfile, employerProfile);
        subscriptionService.deleteSubscription(subscription);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/toSubscribe", method = RequestMethod.POST)
    public ResponseEntity toSubscribeCompany(@RequestParam("vacancyId") Long vacancyId,
                                             @RequestParam("seekerProfileId") Long seekerProfileId,
                                             @RequestParam("tags") Set<String> tags) {

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        EmployerProfile employerProfile = vacancyService.getById(vacancyId).getEmployerProfile();
        Set<Tag> tagSet = new HashSet<>();
        for (String s : tags) {
            tagSet.add(tagService.findByName(s));
        }
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