package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.ReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewVoteDao extends JpaRepository<ReviewVote, Long> {

    List<ReviewVote> findAllByEmployerProfileIdAndSeekerProfileId(Long seekerProfileId, Long employerProfileId);

    ReviewVote findByReviewIdAndSeekerProfileId(Long reviewId, Long seekerProfileId);

    void deleteByReviewId(Long reviewId);
}
