package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.users.UserEmployer;
import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
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
    private EmployerService employerService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateEmployer(@RequestBody UserEmployer userEmployer) {
        Long id = userEmployer.getProfile().getId();
        ProfileEmployer tmpEmployer = employerProfileService.getById(id);

        userEmployer.getProfile().setLogo(tmpEmployer.getLogo());
        userEmployer.getProfile().setReviews(tmpEmployer.getReviews());

        employerProfileService.update(userEmployer.getProfile());
        employerService.update(userEmployer);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editLogo", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ProfileEmployer> updateEmployerLogo(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
        UserEmployer userEmployer = employerService.getById(Long.parseLong(id));
        if (!file.isEmpty()) {
            try {
                byte[] logo = file.getBytes();
                userEmployer.getProfile().setLogo(logo);
                employerProfileService.update(userEmployer.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(userEmployer.getProfile(), HttpStatus.OK);
    }

    @GetMapping("/{employerId}")
    public ResponseEntity<ProfileEmployer> getEmployerById(@PathVariable Long employerId) {
        return new ResponseEntity<>(employerProfileService.getById(employerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{employerId}", method = RequestMethod.GET)
    public ResponseEntity deleteEmployerById(@PathVariable Long employerId) {
        employerService.deleteById(employerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
