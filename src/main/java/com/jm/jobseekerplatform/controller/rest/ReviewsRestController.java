package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerReviewsService;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewsRestController {

    @Autowired
    EmployerReviewsService employerReviewsService;

    @Autowired
    SeekerProfileService seekerProfileService;

    @Autowired
    EmployerProfileService employerProfileService;

    @RequestMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addNewReviews(@RequestBody Map<String, Object> map) {
        try {
            EmployerReviews employerReviews = employerReviewsService.findReviewsBySeekerId(((Number) map.get("seekerProfiles_id")).longValue());
            employerReviews.setEvaluation(Integer.parseInt(map.get("evaluation").toString()));
            employerReviews.setReviews(String.valueOf(map.get("reviews")));
            employerReviewsService.update(employerReviews);
            return new ResponseEntity<String>("{\"status\" : \"Review updated\"}", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            EmployerReviews employerReviews = new EmployerReviews();
            employerReviews.setReviews(String.valueOf(map.get("reviews")));
            employerReviews.setDateReviews(new Date());
            employerReviews.setEvaluation(Integer.parseInt(String.valueOf(map.get("evaluation"))));
            employerReviews.setSeekerProfile((seekerProfileService.getById(((Number) map.get("seekerProfiles_id")).longValue())));
            EmployerProfile employer = employerProfileService.getById(((Number) map.get("employerProfiles_id")).longValue());
            employer.addNewReview(employerReviews);
            employerProfileService.update(employer);
            return new ResponseEntity<String>("{\"status\": \"Review added\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"status\" :" + e.getMessage() + "}", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/delete/{reviewId}")
    @ResponseBody
    public ResponseEntity<String> getUserById(@PathVariable Long reviewId) {
        try {
            employerReviewsService.deleteById(reviewId);
            return new ResponseEntity<>("{\"status\" : \"Review updated\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"status\" :" + e.getMessage() + "}", HttpStatus.BAD_REQUEST);
        }
    }
}
