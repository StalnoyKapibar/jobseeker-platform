package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EmployerProfile employerProfile;

    @JsonBackReference
    @ManyToOne
    private SeekerProfile seekerProfile;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @Column(name = "removal_time")
    private LocalDateTime removalTime = LocalDateTime
            .of(1995, 5,23, 0,0);

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

    public LocalDateTime getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(LocalDateTime removalTime) {
        this.removalTime = removalTime;
    }
}