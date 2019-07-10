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
    public @ResponseBody
    ResponseEntity updateSeekerUser(@RequestBody SeekerUser seekerUser) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerUser.getProfile().getId());

        seekerUser.getProfile().setPhoto(seekerProfile.getPhoto());
        seekerUser.getProfile().setTags(seekerProfile.getTags());
        seekerUser.getProfile().setPortfolios(seekerProfile.getPortfolios());

        seekerProfileService.update(seekerUser.getProfile());
        seekerUserService.update(seekerUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<SeekerProfile> updateSeekerPhoto(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("seekerUserId") String seekerUserId) {
        SeekerUser seekerUser = seekerUserService.getById(Long.parseLong(seekerUserId));
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                seekerUser.getProfile().setPhoto(photo);
                seekerProfileService.update(seekerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(seekerUser.getProfile(), HttpStatus.OK);
    }

//    @RequestMapping(value = "/{seekerProfileId}", method = RequestMethod.GET)
//    public ResponseEntity<SeekerProfile> getSeekerProfileById(@PathVariable Long seekerProfileId) {
//        return new ResponseEntity<>(seekerProfileService.getById(seekerProfileId), HttpStatus.OK);
//    }

    @RequestMapping(value = "/delete/{seekerUserId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long seekerUserId) {
        seekerUserService.deleteById(seekerUserId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
