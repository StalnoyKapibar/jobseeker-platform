package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.EmployerReviewsService;
import com.jm.jobseekerplatform.service.impl.ReviewVoteService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
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

    @Autowired
    ReviewVoteService reviewVoteService;

/*    @RequestMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addNewReviews(@RequestBody Map<String, Object> map) {
        try {
            EmployerProfile employerProfile = employerProfileService.getById(((Number) map.get("employerProfiles_id")).longValue());

            EmployerReviews reviews = employerProfile.getReviews().stream()
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
                employerProfile.addNewReview(newReview);
                employerProfileService.update(employerProfile);
                return new ResponseEntity<String>("{\"status\": \"Review added\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/

    /*@RequestMapping("/delete/{reviewId}")
    @ResponseBody
    public ResponseEntity<String> getUserById(@PathVariable Long reviewId) {
        try {
            employerReviewsService.deleteById(reviewId);
            return new ResponseEntity<>("{\"status\" : \"Review deleted\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"status\" :" + e.getMessage() + "}", HttpStatus.BAD_REQUEST);
        }
    }*/

    @GetMapping(value = "/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployerReviews> findReviewBySeekerIdAndEmployerIdForEdit(@RequestBody Map<String, Object> map) {
        try {
            EmployerProfile employerProfile = employerProfileService.getById(((Number) map.get("employerProfiles_id")).longValue());
            Set<EmployerReviews> reviewsSet = employerProfile.getReviews();
            EmployerReviews reviews = reviewsSet.stream().filter(employerReviews -> employerReviews.getCreatorProfile().getId() == ((Number) map.get("seekerProfiles_id")).longValue()).findFirst().orElse(null);
            if (reviews != null) return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get_all")
    public ResponseEntity<Set<EmployerReviews>> getAllReviewsByEmployerProfileId(@RequestParam("employerProfileId") Long employerProfileId) {
        return new ResponseEntity<>(employerProfileService.getById(employerProfileId).getReviews(), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<EmployerReviews> geReviewtById(@RequestParam("reviewId") Long reviewId) {
        return new ResponseEntity<>(employerReviewsService.getById(reviewId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addNewReview(@RequestBody EmployerReviews reviews,
                                       @RequestParam("seekerProfileId") Long seekerProfileId,
                                       @RequestParam("employerProfileId") Long employerProfileId) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        if (employerProfile.getReviews().stream().anyMatch(reviews1 -> Objects.equals(reviews1.getCreatorProfile().getId(), seekerProfileId))) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        reviews.setCreatorProfile(seekerProfile);
        reviews.setCreatorProfile(seekerProfile);
        reviews.setHeadline(seekerProfile.getFullName());
        employerReviewsService.add(reviews);
        employerProfile.getReviews().add(reviews);
        employerProfileService.update(employerProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity updateReview(@RequestBody EmployerReviews reviews,
                                       @RequestParam("seekerProfileId") Long seekerProfileId) {
        reviews.setCreatorProfile(seekerProfileService.getById(seekerProfileId));
        reviews.setCreatorProfile(seekerProfileService.getById(seekerProfileId));
        employerReviewsService.update(reviews);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete")
    public ResponseEntity deleteReview(@RequestParam("reviewId") Long reviewId) {
        try {
            employerReviewsService.deleteById(reviewId);
            reviewVoteService.deleteByReviewId(reviewId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
