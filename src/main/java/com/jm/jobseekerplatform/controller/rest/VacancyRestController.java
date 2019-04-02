package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    @RequestMapping("/")
    public List<Vacancy> getAllEmployerProfiles() {
        List<Vacancy> employerprofiles = vacancyService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{vacancyId}")
    public Vacancy getUserById(@PathVariable Long vacancyId){
        Vacancy employerProfile = vacancyService.getById(vacancyId);
        return employerProfile;
    }
}
