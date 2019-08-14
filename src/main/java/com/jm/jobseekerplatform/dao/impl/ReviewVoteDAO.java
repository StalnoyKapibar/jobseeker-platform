package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.ReviewVote;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("reviewVoteDAO")
public class ReviewVoteDAO extends AbstractDAO<ReviewVote> {

    public List<ReviewVote> getAllReviewVotesByEmployerProfileAndSeekerProfileId(Long seekerProfileId, Long employerProfileId) {
        return entityManager.createQuery("select v from ReviewVote v where v.employerProfileId =: employerProfileId and v.seekerProfileId =:seekerProfileId", ReviewVote.class)
                .setParameter("employerProfileId", employerProfileId)
                .setParameter("seekerProfileId", seekerProfileId)
                .getResultList();
    }

    public ReviewVote getByReviewAndSeekerProfileId(Long reviewId, Long seekerProfileId) {
        return entityManager.createQuery("select v from ReviewVote v where v.reviewId =: reviewId and v.seekerProfileId =:seekerProfileId", ReviewVote.class)
                .setParameter("reviewId", reviewId)
                .setParameter("seekerProfileId", seekerProfileId)
                .getSingleResult();
    }

    public void deleteByReviewId(Long reviewId) {
        entityManager.createQuery("delete from ReviewVote where review_id =: reviewId")
                .setParameter("reviewId", reviewId)
                .executeUpdate();
    }
}
