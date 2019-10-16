package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminRestController {
    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ResumeService resumeService;

    @RequestMapping(value = "/all/seekers", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumSeekerUsersByAllPeriod() {
        return new ResponseEntity<>(seekerUserService.getAll().size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/all/employers", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumEmployerUsersByAllPeriod() {
        return new ResponseEntity<>(employerUserService.getAll().size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/all/vacancies", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumVacanciesByAllPeriod() {
        return new ResponseEntity<>(vacancyService.getAll().size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/all/resumes", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumResumesByAllPeriod() {
        return new ResponseEntity<>(resumeService.getAll().size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/seekers", params = {"startDate", "endDate"}, method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumSeekerUsersByDatePeriod(@RequestParam("startDate") String start,
                                                                 @RequestParam("endDate") String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<SeekerUser> seekerUserList = seekerUserService.getSeekerUsersByDatePeriod(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter));
        return new ResponseEntity<>(seekerUserList.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/employers", params = {"startDate", "endDate"}, method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumEmployerUsersByDatePeriod(@RequestParam("startDate") String start,
                                                                   @RequestParam("endDate") String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<EmployerUser> employerUserList = employerUserService.getEmployerUsersByDatePeriod(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter));
        return new ResponseEntity<>(employerUserList.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/vacancies", params = {"startDate", "endDate"}, method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumVacanciesByDatePeriod(@RequestParam("startDate") String start,
                                                               @RequestParam("endDate") String end) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Vacancy> vacancyList = vacancyService.getSumVacanciesByDatePeriod(startDate, endDate);
        return new ResponseEntity<>(vacancyList.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/resumes", params = {"startDate", "endDate"}, method = RequestMethod.GET)
    public ResponseEntity<Integer> getResumesUsersByDatePeriod(@RequestParam("startDate") String start,
                                                                   @RequestParam("endDate") String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Resume> resumeList= resumeService.getResumesByDatePeriod(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter));
        return new ResponseEntity<>(resumeList.size(), HttpStatus.OK);
    }
}
