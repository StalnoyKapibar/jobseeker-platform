package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.model.EmployerProfile;
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
    ResponseEntity updateEmployer(@RequestBody Employer employer) {
        Long id = employer.getProfile().getId();
        EmployerProfile tmpEmployer = employerProfileService.getById(id);

        employer.getProfile().setLogo(tmpEmployer.getLogo());
        employer.getProfile().setReviews(tmpEmployer.getReviews());

        employerProfileService.update(employer.getProfile());
        employerService.update(employer);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/editLogo", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<EmployerProfile> updateEmployerLogo(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("id") String id) {
        Employer employer = employerService.getById(Long.parseLong(id));
        if (!file.isEmpty()) {
            try {
                byte[] logo = file.getBytes();
                employer.getProfile().setLogo(logo);
                employerProfileService.update(employer.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(employer.getProfile(), HttpStatus.OK);
    }

    @GetMapping("/{employerId}")
    public ResponseEntity<EmployerProfile> getEmployerById(@PathVariable Long employerId) {
        return new ResponseEntity<>(employerProfileService.getById(employerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{employerId}", method = RequestMethod.GET)
    public ResponseEntity deleteEmployerById(@PathVariable Long employerId) {
        employerService.deleteById(employerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
