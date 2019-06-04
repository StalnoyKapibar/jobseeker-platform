package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @RequestMapping("/")
    public List<Vacancy> getAll() {
        List<Vacancy> vacancies = vacancyService.getAll();
        return vacancies;
    }

    @RequestMapping("/{vacancyId:\\d+}")
    public Vacancy getVacancyById(@PathVariable Long vacancyId) {
        Vacancy vacancy = vacancyService.getById(vacancyId);
        return vacancy;
    }

    @RequestMapping(value = "/{vacancyId:\\d+}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateVacancyState(@PathVariable("vacancyId") Long id, @RequestBody String state) {
        String st = state.substring(1, state.length() - 1);
        State s = State.valueOf(st);
        Vacancy vacancy = vacancyService.getById(id);
        vacancy.setState(s);
        vacancyService.update(vacancy);
    }

    @RequestMapping(value = "/block/{vacancyId}", method = RequestMethod.POST)
    public void blockVacancy(@PathVariable("vacancyId") Long id, @RequestBody int periodInDays) {
        Vacancy vacancy = vacancyService.getById(id);
        if (periodInDays == 0) {
            vacancyService.blockPermanently(vacancy);
        }
        if (periodInDays > 0 && periodInDays < 15) {
            vacancyService.blockTemporary(vacancy, periodInDays);
        }
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addVacancy(@RequestBody Vacancy vacancy, Authentication authentication) {
        if (vacancyService.validateVacancy(vacancy)) {
            vacancyService.addNewVacancyFromRest(vacancy);
            Long employerProfileId = ((Employer) authentication.getPrincipal()).getEmployerProfile().getId();
            Long vacancyId = vacancy.getId();
            employerProfileService.addVacancyToEmployerProfile(employerProfileId, vacancyId);
            return true;
        } else {
            throw new IllegalArgumentException("Some fields is incorrect");
        }
    }
}
