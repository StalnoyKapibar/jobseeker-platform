package com.jm.jobseekerplatform.model.createdByProfile;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CreatedByProfileBase<T extends Profile> implements CreatedByProfile<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = Profile.class)
    private T creatorProfile;

    @Column(name = "headline", nullable = false)
    private String headline;

    public CreatedByProfileBase() {
    }

    public CreatedByProfileBase(T creatorProfile, String headline) {
        this.creatorProfile = creatorProfile;
        this.headline = headline;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @Override
    public T getCreatorProfile() {
        return creatorProfile;
    }

    public void setCreatorProfile(T creatorProfile) {
        this.creatorProfile = creatorProfile;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Override
    public String toString() {
        return "CreatedByProfileBase{" +
                "id=" + id +
                ", creatorProfile=" + creatorProfile +
                ", headline='" + headline + '\'' +
                '}';
    }
}
