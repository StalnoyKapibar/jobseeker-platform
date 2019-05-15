package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerReviewsService;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;

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
            EmployerProfile employer = employerProfileService.getById(((Number) map.get("employerProfiles_id")).longValue());
            EmployerReviews reviews = employer.getReviews().stream()
                    .filter(employerReviews -> employerReviews.getSeekerProfile().getId() == ((Number) map.get("seekerProfiles_id")).longValue())
                    .findFirst().orElse(null);
            if (reviews != null) {
                reviews.setEvaluation(Integer.parseInt(map.get("evaluation").toString()));
                reviews.setReviews(String.valueOf(map.get("reviews")));
                employerReviewsService.update(reviews);
                return new ResponseEntity<String>("{\"status\" : \"Review updated\"}", HttpStatus.OK);
            } else {
                EmployerReviews newReview = new EmployerReviews();
                newReview.setReviews(String.valueOf(map.get("reviews")));
                newReview.setDateReviews(new Date());
                newReview.setEvaluation(Integer.parseInt(String.valueOf(map.get("evaluation"))));
                newReview.setSeekerProfile((seekerProfileService.getById(((Number) map.get("seekerProfiles_id")).longValue())));
                employer.addNewReview(newReview);
                employerProfileService.update(employer);
                return new ResponseEntity<String>("{\"status\": \"Review added\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/delete/{reviewId}")
    @ResponseBody
    public ResponseEntity<String> getUserById(@PathVariable Long reviewId) {
        try {
            employerReviewsService.deleteById(reviewId);
            return new ResponseEntity<>("{\"status\" : \"Review deleted\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"status\" :" + e.getMessage() + "}", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EmployerReviews> findReviewBySeekerIdAndEmloerIdForEdit(@RequestBody Map<String, Object> map) {
        try {
            EmployerProfile employer = employerProfileService.getById(((Number) map.get("employerProfiles_id")).longValue());
            Set<EmployerReviews> reviewsSet = employer.getReviews();
            EmployerReviews reviews = reviewsSet.stream().filter(employerReviews -> employerReviews.getSeekerProfile().getId() == ((Number) map.get("seekerProfiles_id")).longValue()).findFirst().orElse(null);
            if (reviews != null) return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
