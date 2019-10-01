package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.JobExperience;
import com.jm.jobseekerplatform.model.JobExperience;
import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.JobExperienceService;
import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.service.impl.JobExperienceService;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.hibernate.Hibernate;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/resumes")
public class ResumeRestController {

    @Autowired
    SeekerProfileService seekerProfileService;

    @Autowired
    JobExperienceService jobExperienceService;

    @Autowired
    private SeekerUserService seekerUserService;


    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/getbyid/{resumeId}")
    public Resume getResumeById(@PathVariable Long resumeId) {
        return resumeService.getById(resumeId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Page<Resume>> getSearchVacancies(@RequestBody Set<Tag> searchParam,
                                                    @RequestParam("pageCount") int pageCount) {
        if (searchParam.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Page<Resume> page = resumeService.findAllByTags(searchParam, PageRequest.of(pageCount, 10));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/city/page/{page}", method = RequestMethod.POST)
    public Page<Resume> getPageOfResumes(@RequestBody Point point,
                                         @RequestParam("city") String city,
                                         @PathVariable("page") int page) {
        int limit = 10;
        if (city.equals("undefined")) {
            return resumeService.getAllResumes(limit, page);
        } else {
            return resumeService.findResumesByPoint(city, point, limit, page);
        }
    }
    @RolesAllowed({"ROLE_EMPLOYER"})
    @PostMapping("/sendmail")
    public ResponseEntity sendMailToSeeker(@RequestParam("dataID") long resumeID) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        EmployerProfile employerProfile = (((EmployerUser) userService.findByEmail(userName)).getProfile());
        String companyName = employerProfile.getCompanyName();
        SeekerProfile seekerProfile = seekerUserService.getSeekerProfileByResumeID(resumeID);
        SeekerUser seekerUser = seekerUserService.getByProfileId(seekerProfile.getId());
        mailService.sendFeedBackEMail(seekerUser.getEmail(), companyName, employerProfile.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_SEEKER"})
    @RequestMapping(value = "/seeker", method = RequestMethod.POST, produces = "application/json")
    public Page<Resume> getSeekerResumesPage(Authentication authentication) {
        Set<Resume> resumeSet = seekerProfileService.getById(((User) authentication.getPrincipal())
                .getProfile().getId()).getResumes();
        return seekerProfileService.getPageSeekerResumesById(resumeSet, (((User) authentication
                .getPrincipal()).getProfile().getId()));
    }
    @PostMapping("/getfilter")
    public Page<Resume> getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(@RequestBody Map<String, Object> queryParamsMap,
                                                                                             @RequestParam("page") int pageNumber) {
        int pageSize = 10;
        return resumeService.getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(queryParamsMap, pageNumber, pageSize);
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping(value = "/seeker/{seekerProfileId}", method = RequestMethod.POST)
    public Page<Resume> getSeekerResumesPageForEmployer(@PathVariable Long seekerProfileId) {
        Set<Resume> resumeSet = seekerProfileService.getById(seekerProfileId).getResumes();
        return seekerProfileService.getPageSeekerResumesById(resumeSet, seekerProfileId);
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

    @RolesAllowed("ROLE_SEEKER")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addResume(@RequestBody Resume resume,
                             Authentication authentication) {
        if (resumeService.validateResume(resume)) {
            SeekerProfile seekerProfile = (SeekerProfile) Hibernate.unproxy(seekerProfileService
                                                                    .getById(((User) authentication.getPrincipal())
                                                                    .getProfile().getId()));
            resume.setCreatorProfile(seekerProfile);
            Set<JobExperience> validatedExperiences = jobExperienceService
                                                        .validateJobExperiences(resume.getJobExperiences());
            resume.setJobExperiences(validatedExperiences);
            resumeService.addResume(resume);
            Set<Resume> resumes = seekerProfileService.getById(seekerProfile.getId()).getResumes();
            resumes.add(resume);
            seekerProfile.setResumes(resumes);
            seekerProfileService.update(seekerProfile);
            return true;
        } else {
            throw new IllegalArgumentException("Some fields are incorrect");
        }
    }

    @RolesAllowed("ROLE_SEEKER")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public boolean updateResume(@RequestBody Resume resume, Authentication authentication) {
        if (resumeService.validateResume(resume)) {
            SeekerProfile seekerProfile = (SeekerProfile) Hibernate.unproxy(seekerProfileService
                                                                    .getById(((User) authentication.getPrincipal())
                                                                    .getProfile().getId()));
            Set<JobExperience> validatedExperiences = jobExperienceService
                                                        .validateJobExperiences(resume.getJobExperiences());
            resume.setJobExperiences(validatedExperiences);
            resume.setCreatorProfile(seekerProfile);
            resumeService.updateResume(resume);
            seekerProfileService.update(seekerProfile);
            return true;
        }
        return false;
    }
}
