package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfileBase;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "resumes")
public class Resume extends CreatedByProfileBase<SeekerProfile> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @Column(name = "salarymin")
    private Integer salaryMin;

    @Column(name = "salarymax")
    private Integer salaryMax;

    @OneToMany(cascade = CascadeType.MERGE)
    private Set<JobExperience> jobExperiences;

    @OneToOne(fetch = FetchType.LAZY)
    private City city;

    @OneToOne(fetch = FetchType.LAZY)
    private Point coordinates;

    public Resume() {
    }

    public Resume(SeekerProfile creatorProfile, Set<Tag> tags, Integer salaryMin, Integer salaryMax, Set<JobExperience> jobExperiences, City city, Point coordinates) {
        super(creatorProfile);
        this.tags = tags;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobExperiences = jobExperiences;
        this.city = city;
        this.coordinates = coordinates;
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
    public String getTypeName() {
        return "Резюме";
    }

}
