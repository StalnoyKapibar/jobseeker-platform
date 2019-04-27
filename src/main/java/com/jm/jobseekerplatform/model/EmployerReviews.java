package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employer_reviews")
public class EmployerReviews implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reviews")
    private String reviews;

    @Column(name = "date_revives")
    private Date dateReviews;

    @Column(name = "evaluation")
    private Integer evaluation;

    @ManyToOne
    @JoinColumn(name = "employerProfiles_id")
    private EmployerProfile employerProfiles;

    @ManyToOne
    @JoinColumn(name = "seekerProfiles_id")
    private SeekerProfile seekerProfile;

    public EmployerReviews() {
    }

    public EmployerReviews(String reviews, Date dateReviews, Integer evaluation, EmployerProfile employerProfiles, SeekerProfile seeker) {
        this.reviews = reviews;
        this.dateReviews = dateReviews;
        this.evaluation = evaluation;
        this.employerProfiles = employerProfiles;
        this.seekerProfile = seeker;
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

    public EmployerProfile getEmployerProfiles() {
        return employerProfiles;
    }

    public void setEmployerProfiles(EmployerProfile employerProfiles) {
        this.employerProfiles = employerProfiles;
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeeker(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployerReviews that = (EmployerReviews) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (reviews != null ? !reviews.equals(that.reviews) : that.reviews != null) return false;
        if (dateReviews != null ? !dateReviews.equals(that.dateReviews) : that.dateReviews != null) return false;
        if (evaluation != null ? !evaluation.equals(that.evaluation) : that.evaluation != null) return false;
        if (employerProfiles != null ? !employerProfiles.equals(that.employerProfiles) : that.employerProfiles != null)
            return false;
        return seekerProfile != null ? seekerProfile.equals(that.seekerProfile) : that.seekerProfile == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        result = 31 * result + (dateReviews != null ? dateReviews.hashCode() : 0);
        result = 31 * result + (evaluation != null ? evaluation.hashCode() : 0);
        result = 31 * result + (employerProfiles != null ? employerProfiles.hashCode() : 0);
        result = 31 * result + (seekerProfile != null ? seekerProfile.hashCode() : 0);
        return result;
    }
}
