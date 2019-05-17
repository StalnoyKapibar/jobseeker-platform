package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    @RequestMapping("/")
    public List<Vacancy> getAllEmployerProfiles() {
        List<Vacancy> vacancies = vacancyService.getAll();
        return vacancies;
    }

    @GetMapping("/{vacancyId}")
    public Vacancy getUserById(@PathVariable Long vacancyId){
        Vacancy vacancy = vacancyService.getById(vacancyId);
        return vacancy;
    }

    @PutMapping("/{vacancyId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable("vacancyId") Long id,  @RequestBody String state) {
        String st = state.substring(1, state.length() - 1);
        State s = State.valueOf(st);
        Vacancy vacancy = vacancyService.getById(id);
        vacancy.setState(s);
        vacancyService.update(vacancy);
    }
}
