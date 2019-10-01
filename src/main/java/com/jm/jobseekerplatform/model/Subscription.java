package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private EmployerProfile employerProfile;

    @JsonBackReference
    @ManyToOne
    private SeekerProfile seekerProfile;

    @ManyToMany
    private Set<Tag> tags;

    public Subscription() {
    }

    public Subscription(EmployerProfile employerProfile, SeekerProfile seekerProfile, Set<Tag> tags) {
        this.employerProfile = employerProfile;
        this.seekerProfile = seekerProfile;
        this.tags = tags;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }
}