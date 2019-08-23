package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.createdByProfile.CreatedBySeekerProfileBase;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employer_reviews")
public class EmployerReviews extends CreatedBySeekerProfileBase implements Serializable, Comparable<EmployerReviews> {

    @Column(name = "reviews")
    private String reviews;

    @Column(name = "date_revives")
    private Date dateReviews;

    @Column(name = "evaluation")
    private Integer evaluation;

    @Column(name = "review_like")
    private int like;

    @Column(name = "review_dislike")
    private int dislike;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "seekerProfile")
    private SeekerProfile seekerProfile;

    public EmployerReviews() {
    }

    public EmployerReviews(SeekerProfile creatorProfile, String headline, String reviews, Date dateReviews, Integer evaluation, SeekerProfile seekerProfile) {
        super(creatorProfile, headline);
        this.reviews = reviews;
        this.dateReviews = dateReviews;
        this.evaluation = evaluation;
        this.seekerProfile = seekerProfile;
    }

    @Override
    public String getHeadline() {
        return this.seekerProfile.getFullName();
    }

    @Override
    public String getTypeName() {
        return "Отзыв";
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Date getDateReviews() {
        return dateReviews;
    }

    public void setDateReviews(Date dateReviews) {
        this.dateReviews = dateReviews;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public void incrementLike() {
        ++this.like;
    }

    public void incrementDislike() {
        ++this.dislike;
    }

    public void decrementLike() {
        --this.like;
    }

    public void decrementDislike() {
        --this.dislike;
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }


    @Override
    public String toString() {
        return "EmployerReviews{" +
                super.toString() +
                "reviews='" + reviews + '\'' +
                ", dateReviews=" + dateReviews +
                ", evaluation=" + evaluation +
                ", like=" + like +
                ", dislike=" + dislike +
                ", seekerProfile=" + seekerProfile +
                '}';
    }

    @Override
    public int compareTo(EmployerReviews o) {
        if (this.getEvaluation().equals(o.getEvaluation())) {
            return 0;
        } else if (this.getEvaluation() > o.getEvaluation()) {
            return -1;
        } else {
            return 1;
        }
    }
}
