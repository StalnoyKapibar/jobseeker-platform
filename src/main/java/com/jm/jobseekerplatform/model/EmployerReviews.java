package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "employer_reviews")
public class EmployerReviews implements Serializable, Comparable<EmployerReviews> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "seekerProfile")
    private SeekerProfile seekerProfile;

    public EmployerReviews() {
    }

    public EmployerReviews(String reviews, Date dateReviews, Integer evaluation, SeekerProfile seekerProfile) {
        this.reviews = reviews;
        this.dateReviews = dateReviews;
        this.evaluation = evaluation;
        this.seekerProfile = seekerProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployerReviews that = (EmployerReviews) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(reviews, that.reviews) &&
                Objects.equals(dateReviews, that.dateReviews) &&
                Objects.equals(evaluation, that.evaluation) &&
                Objects.equals(seekerProfile, that.seekerProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviews, dateReviews, evaluation, seekerProfile);
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
