package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.ReviewVote;
import com.jm.jobseekerplatform.service.impl.EmployerReviewsService;
import com.jm.jobseekerplatform.service.impl.ReviewVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reviewVote")
public class ReviewVoteRestController {

    @Autowired
    EmployerReviewsService employerReviewsService;

    @Autowired
    ReviewVoteService reviewVoteService;

    @GetMapping("/get_all")
    @ResponseBody
    public ResponseEntity<Set<ReviewVote>> getAllReviewVotesByEmployerProfileAndSeekerProfileId(@RequestParam("employerProfileId") Long employerProfileId,
                                                                                                @RequestParam("seekerProfileId") Long seekerProfileId) {
        List<ReviewVote> reviewVotes = reviewVoteService.getAllReviewVotesByEmployerProfileAndSeekerProfileId(seekerProfileId, employerProfileId);
        return new ResponseEntity<>(new HashSet<>(reviewVotes), HttpStatus.OK);
    }

    @PostMapping("/add_like")
    @ResponseBody
    public ResponseEntity<EmployerReviews> addNewReviewLike(@RequestBody ReviewVote reviewVote) {
        EmployerReviews employerReviews = employerReviewsService.getById(reviewVote.getReviewId());
        employerReviews.incrementLike();
        employerReviewsService.update(employerReviews);
        reviewVoteService.add(reviewVote);
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }

    @PostMapping("/add_dislike")
    @ResponseBody
    public ResponseEntity<EmployerReviews> addNewReviewDislike(@RequestBody ReviewVote reviewVote) {
        EmployerReviews employerReviews = employerReviewsService.getById(reviewVote.getReviewId());
        employerReviews.incrementDislike();
        employerReviewsService.update(employerReviews);
        reviewVoteService.add(reviewVote);
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }

    @PostMapping("/update_like")
    @ResponseBody
    public ResponseEntity<EmployerReviews> updateReviewLike(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("seekerProfileId") Long seekerProfileId) {
        ReviewVote reviewVote = reviewVoteService.getByReviewAndSeekerProfileId(reviewId, seekerProfileId);
        reviewVote.setLike(true);
        reviewVote.setDislike(false);
        reviewVoteService.update(reviewVote);
        EmployerReviews employerReviews = employerReviewsService.getById(reviewId);
        employerReviews.incrementLike();
        employerReviews.decrementDislike();
        employerReviewsService.update(employerReviews);
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }

    @PostMapping("/update_dislike")
    @ResponseBody
    public ResponseEntity<EmployerReviews> updateReviewDislike(@RequestParam("reviewId") Long reviewId,
                                                               @RequestParam("seekerProfileId") Long seekerProfileId) {
        ReviewVote reviewVote = reviewVoteService.getByReviewAndSeekerProfileId(reviewId, seekerProfileId);
        reviewVote.setLike(false);
        reviewVote.setDislike(true);
        reviewVoteService.update(reviewVote);
        EmployerReviews employerReviews = employerReviewsService.getById(reviewId);
        employerReviews.decrementLike();
        employerReviews.incrementDislike();
        employerReviewsService.update(employerReviews);
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }

    @GetMapping("/delete_like")
    @ResponseBody
    public ResponseEntity<EmployerReviews> deleteReviewLike(@RequestParam("reviewId") Long reviewId,
                                                            @RequestParam("seekerProfileId") Long seekerProfileId) {
        EmployerReviews employerReviews = employerReviewsService.getById(reviewId);
        employerReviews.decrementLike();
        employerReviewsService.update(employerReviews);
        reviewVoteService.delete(reviewVoteService.getByReviewAndSeekerProfileId(reviewId, seekerProfileId));
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }

    @GetMapping("/delete_dislike")
    @ResponseBody
    public ResponseEntity<EmployerReviews> deleteReviewDislike(@RequestParam("reviewId") Long reviewId,
                                                               @RequestParam("seekerProfileId") Long seekerProfileId) {
        reviewVoteService.delete(reviewVoteService.getByReviewAndSeekerProfileId(reviewId, seekerProfileId));
        EmployerReviews employerReviews = employerReviewsService.getById(reviewId);
        employerReviews.decrementDislike();
        employerReviewsService.update(employerReviews);
        return new ResponseEntity<>(employerReviews, HttpStatus.OK);
    }
}
