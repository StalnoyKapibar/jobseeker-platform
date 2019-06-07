package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        EmployerProfile tmpEmployer = employerProfileService.getById(employer.getEmployerProfile().getId());
        employer.getEmployerProfile().setLogo(tmpEmployer.getLogo());
        employer.getEmployerProfile().setVacancies(tmpEmployer.getVacancies());
        employer.getEmployerProfile().setReviews(tmpEmployer.getReviews());
        employerProfileService.update(employer.getEmployerProfile());
        employerService.update(employer);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{employerId}")
    public ResponseEntity<EmployerProfile> getSeekerById(@PathVariable Long employerId) {
        return new ResponseEntity<>(employerProfileService.getById(employerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{employerId}", method = RequestMethod.GET)
    public ResponseEntity deleteSeekerById(@PathVariable Long employerId) {
        employerService.deleteById(employerId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
