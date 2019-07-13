package com.jm.jobseekerplatform.controller.rest;

import com.google.maps.errors.ApiException;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private EmployerProfileService employerProfileService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @Autowired
    private TagService tagService;

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
            EmployerProfile employerProfile = ((Employer) authentication.getPrincipal()).getProfile();
            vacancy.setEmployerProfile(employerProfile);
            vacancyService.addNewVacancyFromRest(vacancy);
            return true;
        } else {
            throw new IllegalArgumentException("Some fields is incorrect");
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Set<Vacancy>> getSearchVacancies(@RequestBody Set<Tag> searchParam, @RequestParam("pageCount") int pageCount) {
        if (searchParam.isEmpty()) {
            return new ResponseEntity<>(new HashSet<>(), HttpStatus.OK);
        }
        List<Vacancy> list = vacancyService.findAllByTags(searchParam,PageRequest.of(pageCount, 10,
                new Sort(Sort.Direction.ASC, "id"))).getContent();
        return new ResponseEntity<>(new HashSet<>(list), HttpStatus.OK);
    }

    @RequestMapping("/{city}/page/{page}")
    public Page<Vacancy> getPageOfVacancies(@RequestBody Point point, @PathVariable("city") String city, @PathVariable("page") int page, Authentication authentication) throws InterruptedException, ApiException, IOException {
        int limit = 10;

        if (authentication == null || !authentication.isAuthenticated()) {
            return vacancyService.findVacanciesByPoint(city, point, limit, page);
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                Set<Tag> tags = ((Seeker) authentication.getPrincipal()).getProfile().getTags();
                return vacancyService.findVacanciesByTagsAndByPoint(city, point, tags, limit, page);
            }
        }
        return vacancyService.findVacanciesByPoint(city, point, limit, page);
    }
}
