package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "review_vote")
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
}
