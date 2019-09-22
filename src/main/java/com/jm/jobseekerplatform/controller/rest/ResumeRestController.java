package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@RestController
@RequestMapping("api/resumes")
public class ResumeRestController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/getbyid/{resumeId}")
    public Resume getResumeById(@PathVariable Long resumeId) {
        return resumeService.getById(resumeId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Page<Resume>> getSearchVacancies(@RequestBody Set<Tag> searchParam, @RequestParam("pageCount") int pageCount) {
        if (searchParam.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Page<Resume> page = resumeService.findAllByTags(searchParam, PageRequest.of(pageCount, 10));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/city/page/{page}", method = RequestMethod.POST)
    public Page<Resume> getPageOfResumes(@RequestBody Point point, @RequestParam("city") String city, @PathVariable("page") int page) {
        int limit = 10;
        if (city.equals("undefined")) {
            return resumeService.getAllResumes(limit, page);
        } else {
            return resumeService.findResumesByPoint(city, point, limit, page);
        }
    }

    @RolesAllowed({"ROLE_SEEKER"})
	@RequestMapping(value = "/seeker", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Set<Resume>> getSeekerResumesPage(Authentication authentication) {
        Set<Resume> resumeSet = seekerProfileService.getById(((User) authentication.getPrincipal())
                .getProfile().getId()).getResumes();
        return ResponseEntity.ok(resumeSet);
	}

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping(value = "/seeker/{seekerProfileId}", method = RequestMethod.POST)
    public ResponseEntity<Set<Resume>> getSeekerResumesPageForEmployer(@PathVariable Long seekerProfileId) {
        Set<Resume> resumeSet = seekerProfileService.getById(seekerProfileId).getResumes();
        return ResponseEntity.ok(resumeSet);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSeekerUser(@RequestBody Resume resume) {
        resumeService.update(resume);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{resumeId}", method = RequestMethod.GET)
    public ResponseEntity deleteResumeById(@PathVariable Long resumeId) {
        resumeService.deleteByResumeId(resumeId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
