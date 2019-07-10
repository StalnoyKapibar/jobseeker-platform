package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
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
public class EmployerRestController {
    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateEmployer(@RequestBody EmployerUser employerUser) {
        Long id = employerUser.getProfile().getId();
        ProfileEmployer tmpEmployer = employerProfileService.getById(id);

        employerUser.getProfile().setLogo(tmpEmployer.getLogo());
        employerUser.getProfile().setReviews(tmpEmployer.getReviews());

        employerProfileService.update(employerUser.getProfile());
        employerUserService.update(employerUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editLogo", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ProfileEmployer> updateEmployerLogo(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
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

    @GetMapping("/{employerId}")
    public ResponseEntity<ProfileEmployer> getEmployerById(@PathVariable Long employerId) {
        return new ResponseEntity<>(employerProfileService.getById(employerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{employerId}", method = RequestMethod.GET)
    public ResponseEntity deleteEmployerById(@PathVariable Long employerId) {
        employerUserService.deleteById(employerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
