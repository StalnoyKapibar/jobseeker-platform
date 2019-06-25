package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/seeker")
public class SeekerRestController {
    @Autowired
    private SeekerService seekerService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateSeeker(@RequestBody Seeker seeker) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seeker.getSeekerProfile().getId());
        seeker.getSeekerProfile().setPhoto(seekerProfile.getPhoto());
        seeker.getSeekerProfile().setTags(seekerProfile.getTags());
        seeker.getSeekerProfile().setPortfolios(seekerProfile.getPortfolios());
        seekerProfileService.update(seeker.getSeekerProfile());
        seekerService.update(seeker);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<SeekerProfile> updateSeekerPhoto(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
        Seeker seeker = seekerService.getById(Long.parseLong(id));
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                seeker.getSeekerProfile().setPhoto(photo);
                seekerProfileService.update(seeker.getSeekerProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(seeker.getSeekerProfile(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity<SeekerProfile> getSeekerById(@PathVariable("seekerId") Long seekerId) {
        return new ResponseEntity<>(seekerProfileService.getById(seekerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{seekerId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long seekerId) {
        seekerService.deleteById(seekerId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/seeker_id", method = RequestMethod.GET)
    private ResponseEntity<String> getId(Authentication authentication) {
        String id = "";
        if (authentication == null || !authentication.isAuthenticated()) {
            id = "";
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                Seeker seeker = (Seeker) authentication.getPrincipal();
                id = Long.toString(seeker.getSeekerProfile().getId());
            }
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
