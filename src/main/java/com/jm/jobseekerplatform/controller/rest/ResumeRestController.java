package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/resumes")
public class ResumeRestController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/{page}")
    public Page<Resume> getAllResumes(@PathVariable("page") int page) {
        int limit = 10;
        return resumeService.getAllResumes(limit, page);
    }

    @RequestMapping("/getbyid/{resumeId}")
    public Resume getResumeById(@PathVariable Long resumeId) {
        return resumeService.getById(resumeId);
    }
}
