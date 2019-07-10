package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
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
public class SeekerRestController {
    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateSeeker(@RequestBody SeekerUser seekerUser) {
        ProfileSeeker profileSeeker = seekerProfileService.getById(seekerUser.getProfile().getId());
        seekerUser.getProfile().setPhoto(profileSeeker.getPhoto());
        seekerUser.getProfile().setTags(profileSeeker.getTags());
        seekerUser.getProfile().setPortfolios(profileSeeker.getPortfolios());
        seekerProfileService.update(seekerUser.getProfile());
        seekerUserService.update(seekerUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ProfileSeeker> updateSeekerPhoto(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
        SeekerUser seekerUser = seekerUserService.getById(Long.parseLong(id));
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

    @RequestMapping(value = "/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity<ProfileSeeker> getSeekerById(@PathVariable Long seekerId) {
        return new ResponseEntity<>(seekerProfileService.getById(seekerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long seekerId) {
        seekerUserService.deleteById(seekerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
