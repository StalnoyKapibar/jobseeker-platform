package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
import com.jm.jobseekerplatform.model.users.UserSeeker;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
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
    private SeekerService seekerService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateSeeker(@RequestBody UserSeeker userSeeker) {
        ProfileSeeker profileSeeker = seekerProfileService.getById(userSeeker.getProfile().getId());
        userSeeker.getProfile().setPhoto(profileSeeker.getPhoto());
        userSeeker.getProfile().setTags(profileSeeker.getTags());
        userSeeker.getProfile().setPortfolios(profileSeeker.getPortfolios());
        seekerProfileService.update(userSeeker.getProfile());
        seekerService.update(userSeeker);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ProfileSeeker> updateSeekerPhoto(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
        UserSeeker userSeeker = seekerService.getById(Long.parseLong(id));
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                userSeeker.getProfile().setPhoto(photo);
                seekerProfileService.update(userSeeker.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(userSeeker.getProfile(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity<ProfileSeeker> getSeekerById(@PathVariable Long seekerId) {
        return new ResponseEntity<>(seekerProfileService.getById(seekerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long seekerId) {
        seekerService.deleteById(seekerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
