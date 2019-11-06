package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfileBase;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "resumes")
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class Resume extends CreatedByProfileBase<SeekerProfile> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "headline", nullable = false)
    private String headline;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @Column(name = "salarymin")
    private Integer salaryMin;

    @Column(name = "salarymax")
    private Integer salaryMax;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<JobExperience> jobExperiences;

    @OneToOne(fetch = FetchType.LAZY)
    private City city;

    @OneToOne(fetch = FetchType.LAZY)
    private Point coordinates;

    public Resume() {
    }

    public Resume(SeekerProfile creatorProfile, String headline, Set<Tag> tags, Integer salaryMin, Integer salaryMax,
                  Set<JobExperience> jobExperiences, City city, Point coordinates, LocalDateTime date) {
        super(creatorProfile, headline);
        this.headline = headline;
        this.tags = tags;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobExperiences = jobExperiences;
        this.city = city;
        this.coordinates = coordinates;
        this.date = date;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }

    public Set<JobExperience> getJobExperiences() {
        return jobExperiences;
    }

    public void setJobExperiences(Set<JobExperience> jobExperiences) {
        this.jobExperiences = jobExperiences;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String getTypeName() {
        return "Резюме";
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
