package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.VacancyPageDTO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @RequestMapping("/")
    public List<Vacancy> getAll() {
        return vacancyService.getAll();
    }

    @RequestMapping("/{vacancyId:\\d+}")
    public Vacancy getVacancyById(@PathVariable Long vacancyId) {
       return vacancyService.getById(vacancyId);
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/{vacancyId:\\d+}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateVacancyState(@PathVariable("vacancyId") Long vacancyId, @RequestBody String state) {
        String st = state.substring(1, state.length() - 1);
        State s = State.valueOf(st);
        Vacancy vacancy = vacancyService.getById(vacancyId);
        vacancy.setState(s);
        vacancyService.update(vacancy);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @RequestMapping(value = "/block/{vacancyId}", method = RequestMethod.POST)
    public void blockVacancy(@PathVariable("vacancyId") Long vacancyId, @RequestBody int periodInDays) {
        Vacancy vacancy = vacancyService.getById(vacancyId);
        if (periodInDays == 0) {
            vacancyService.blockPermanently(vacancy);
        }
        if (periodInDays > 0 && periodInDays < 15) {
            vacancyService.blockTemporary(vacancy, periodInDays);
        }
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addVacancy(@RequestBody Vacancy vacancy, Authentication authentication) {
        if (vacancyService.validateVacancy(vacancy)) {
            EmployerProfile employerProfile = ((EmployerUser) authentication.getPrincipal()).getProfile();
            vacancy.setEmployerProfile(employerProfile);
            vacancyService.addNewVacancyFromRest(vacancy);
            return true;
        } else {
            throw new IllegalArgumentException("Some fields is incorrect");
        }
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public boolean updateVacancy(@RequestBody Vacancy vacancy, Authentication authentication) {
        if (vacancyService.validateVacancy(vacancy) & vacancyService.getById(vacancy.getId()).getCreatorProfile().getId() == (((EmployerUser) authentication.getPrincipal()).getProfile().getId())) {
            return vacancyService.updateVacancy(vacancy);
        }
        return false;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Page<Vacancy>> getSearchVacancies(@RequestBody Set<Tag> searchParam, @RequestParam("pageCount") int pageCount) {
        if (searchParam.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Page<Vacancy> page = vacancyService.findAllByTags(searchParam, PageRequest.of(pageCount, 10));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/city/page/{page}", method = RequestMethod.POST)
    public Page<Vacancy> getPageOfVacancies(@RequestBody Point point, @RequestParam("city") String city, @PathVariable("page") int page, Authentication authentication) {
        int limit = 10;
        if (authentication == null || !authentication.isAuthenticated()) {
            if (city.equals("undefined")) {
                List<Vacancy> all = vacancyService.getAll();
                Collections.shuffle(all);
                return new VacancyPageDTO(all.subList(0, limit), page);
            } else {
                return vacancyService.findVacanciesByPoint(city, point, limit, page);
            }
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                Set<Tag> tags = ((SeekerProfile)((User)authentication.getPrincipal()).getProfile()).getTags();
                return vacancyService.findVacanciesByTagsAndByPoint(city, point, tags, limit, page);
            }
        }
        return vacancyService.findVacanciesByPoint(city, point, limit, page);
    }

    @PostMapping("/delete")
    @ResponseBody
    public List<Vacancy> getSearchUserProfiles(@RequestBody Vacancy vacancy) throws JSONException {
        long id = vacancy.getId();
        Vacancy delVacancy = vacancyService.getById(id);
        vacancyService.delete(delVacancy);
        return vacancyService.getAll();
    }
}
