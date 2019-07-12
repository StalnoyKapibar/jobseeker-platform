package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employer")
public class EmployerUserRestController {
    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateEmployer(@RequestBody EmployerUser employerUser) {
        Long employerProfileId = employerUser.getProfile().getId();
        EmployerProfile tmpEmployer = employerProfileService.getById(employerProfileId);

        employerUser.getProfile().setLogo(tmpEmployer.getLogo());
        employerUser.getProfile().setReviews(tmpEmployer.getReviews());

        employerProfileService.update(employerUser.getProfile());
        employerUserService.update(employerUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editLogo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<EmployerProfile> updateEmployerLogo(@RequestParam(value = "file", required = false) MultipartFile file,
                                                                            @RequestParam("id") String id) {
        EmployerUser employerUser = employerUserService.getById(Long.parseLong(id));
        if (!file.isEmpty()) {
            try {
                byte[] logo = file.getBytes();
                employerUser.getProfile().setLogo(logo);
                employerProfileService.update(employerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(employerUser.getProfile(), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{employerUserId}", method = RequestMethod.GET)
    public ResponseEntity deleteEmployerUserById(@PathVariable Long employerUserId) {
        employerUserService.deleteById(employerUserId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
