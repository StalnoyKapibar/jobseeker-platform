package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.ReviewVoteDao;
import com.jm.jobseekerplatform.model.ReviewVote;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reviewVoteService")
public class ReviewVoteService extends AbstractService<ReviewVote> {

    @Autowired
    private ReviewVoteDao reviewVoteDao;

    public List<ReviewVote> getAllReviewVotesByEmployerProfileAndSeekerProfileId(Long seekerProfileId, Long employerProfileId) {
        return reviewVoteDao.findAllByEmployerProfileIdAndSeekerProfileId(seekerProfileId, employerProfileId);
    }

    public ReviewVote getByReviewAndSeekerProfileId(Long reviewId, Long seekerProfileId) {
        return reviewVoteDao.findByReviewIdAndSeekerProfileId(reviewId, seekerProfileId);
    }

    public void deleteByReviewId(Long reviewId) {
        reviewVoteDao.deleteByReviewId(reviewId);
    }
}
