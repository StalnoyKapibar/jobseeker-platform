package com.jm.jobseekerplatform.model.createdByProfile;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //todo (Nick Dolgopolov) проверить @Entity, @Inheritance, @MappedSuperclass
public abstract class CreatedByProfileBase<T extends Profile> implements CreatedByProfile<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, targetEntity = Profile.class)
    private T creatorProfile;

    public CreatedByProfileBase() {
    }

    public CreatedByProfileBase(T creatorProfile) {
        this.creatorProfile = creatorProfile;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployerProfile(T creatorProfile) {
        this.creatorProfile = creatorProfile;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @Override
    public T getCreatorProfile() {
        return creatorProfile;
    }

    @Override
    public String toString() {
        return "CreatedByProfileBase{" +
                "id=" + id +
                ", creatorProfile=" + creatorProfile +
                '}';
    }
}
