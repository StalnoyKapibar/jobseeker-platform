package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "employer_reviews")
public class EmployerReviews implements Serializable, Comparable<EmployerReviews>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reviews")
    private String reviews;

    @Column(name = "date_revives")
    private Date dateReviews;

    @Column(name = "evaluation")
    private Integer evaluation;

    @OneToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="profileSeeker")
    private ProfileSeeker profileSeeker;

    public EmployerReviews() {
    }

    public EmployerReviews(String reviews, Date dateReviews, Integer evaluation, ProfileSeeker profileSeeker) {
        this.reviews = reviews;
        this.dateReviews = dateReviews;
        this.evaluation = evaluation;
        this.profileSeeker = profileSeeker;
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

    public ProfileSeeker getProfileSeeker() {
        return profileSeeker;
    }

    public void setProfileSeeker(ProfileSeeker profileSeeker) {
        this.profileSeeker = profileSeeker;
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
                Objects.equals(profileSeeker, that.profileSeeker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviews, dateReviews, evaluation, profileSeeker);
    }

    @Override
    public int compareTo(EmployerReviews o) {
        if (this.getEvaluation().equals(o.getEvaluation())){
            return 0;
        }else if (this.getEvaluation() > o.getEvaluation()){
            return -1;
        }else {
            return 1;
        }
    }
}
