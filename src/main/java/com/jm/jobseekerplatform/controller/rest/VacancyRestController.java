package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.VacancyPageDTO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.service.impl.SeekerHistoryService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyRestController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    private GrantedAuthority roleSeeker = new SimpleGrantedAuthority("ROLE_SEEKER");

    @Autowired
    private SeekerHistoryService seekerHistoryService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployerUserService employerUserService;

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
            vacancy.setCreatorProfile(employerProfile);
            vacancy.setCreationDate(new Date(System.currentTimeMillis()));
            vacancyService.addNewVacancyFromRest(vacancy);
            return true;
        } else {
            throw new IllegalArgumentException("Some fields is incorrect");
        }
    }

    @RolesAllowed({"ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public boolean updateVacancy(@RequestBody Vacancy vacancy, Authentication authentication) {
        if (vacancyService.validateVacancy(vacancy) & (vacancyService.getById(vacancy.getId()).getCreatorProfile().getId().equals(((EmployerUser) authentication.getPrincipal()).getProfile().getId()))) {
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
                return vacancyService.findVacanciesByPointWithLimitAndPaging(city, point, limit, page);
            }
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                SeekerProfile profile = (SeekerProfile) ((User) authentication.getPrincipal()).getProfile();
                return vacancyService.getVacanciesSortedByCityTagsViews(profile.getId(), city, point, limit, page);
            }
        }
        return vacancyService.findVacanciesByPointWithLimitAndPaging(city, point, limit, page);
    }

	@RolesAllowed({"ROLE_SEEKER"})
	@PostMapping("/sendmailvac")
	public ResponseEntity sendMailToEmployer(@RequestParam("dataID") long vacancyID) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		SeekerProfile seekerProfile = (((SeekerUser) userService.findByEmail(userName)).getProfile());
		EmployerProfile employerProfile = employerUserService.getEmployerProfileByVacancyID(vacancyID);
		String seekerFullName = seekerProfile.getFullName();
		EmployerUser employerUser = employerUserService.getByProfileId(employerProfile.getId());
		mailService.sendFeedBackEMailVacancy(employerUser.getEmail(), seekerFullName, seekerProfile.getId());
		return new ResponseEntity(HttpStatus.OK);
	}

}
