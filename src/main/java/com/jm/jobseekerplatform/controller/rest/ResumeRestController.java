package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/resumes")
public class ResumeRestController {
    @Autowired
    ResumeService resumeService;


    @RequestMapping("/")
    public Page<Resume> getAllResumes(){
        List<Resume> resumes= resumeService.getAll();
        return new ResumePageDTO(resumes);
    }
}
