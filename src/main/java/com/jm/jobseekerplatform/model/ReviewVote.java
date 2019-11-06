package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_vote")
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class ReviewVote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "seeker_profile_id")
    private Long seekerProfileId;

    @Column(name = "employer_profile_id")
    private Long employerProfileId;

    @Column(name = "is_like")
    @JsonProperty
    private boolean isLike;

    @Column(name = "is_dislike")
    @JsonProperty
    private boolean isDislike;

    @Column(name = "removal_time")
    private LocalDateTime removalTime = LocalDateTime
            .of(1995, 5,23, 0,0);

    public ReviewVote() {
    }

    public ReviewVote(Long reviewId, Long seekerProfileId, Long employerProfileId, boolean isLike, boolean isDislike) {
        this.reviewId = reviewId;
        this.seekerProfileId = seekerProfileId;
        this.employerProfileId = employerProfileId;
        this.isLike = isLike;
        this.isDislike = isDislike;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getSeekerProfileId() {
        return seekerProfileId;
    }

    public void setSeekerProfileId(Long seekerProfileId) {
        this.seekerProfileId = seekerProfileId;
    }

    public Long getEmployerProfileId() {
        return employerProfileId;
    }

    public void setEmployerProfileId(Long employerProfileId) {
        this.employerProfileId = employerProfileId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    public LocalDateTime getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(LocalDateTime removalTime) {
        this.removalTime = removalTime;
    }
}
