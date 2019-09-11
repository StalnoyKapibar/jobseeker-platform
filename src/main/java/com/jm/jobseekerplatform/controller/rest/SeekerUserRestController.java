package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/seeker")
public class SeekerUserRestController {
    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSeekerUser(@RequestBody SeekerUser seekerUser) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerUser.getProfile().getId());
        seekerUser.getProfile().setLogo(seekerProfile.getLogo());
        seekerUser.getProfile().setTags(seekerProfile.getTags());
        seekerUser.getProfile().setPortfolios(seekerProfile.getPortfolios());

        seekerProfileService.update(seekerUser.getProfile());
        seekerUserService.update(seekerUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public ResponseEntity<SeekerProfile> updateSeekerPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
                                                           @RequestParam("seekerUserId") Long seekerUserId) {
        seekerProfileService.updatePhoto(seekerUserId,file);
        SeekerUser seekerUser = seekerUserService.getById(seekerUserId);
        return new ResponseEntity<>(seekerUser.getProfile(), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{seekerUserId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long seekerUserId) {
        seekerUserService.deleteById(seekerUserId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
