package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfileBase;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "resumes")
public class Resume extends CreatedByProfileBase<SeekerProfile> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place", nullable = false)
    private String place;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private SeekerProfile seekerProfile;

    public Resume() {
    }

    public Resume(String place) {
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getTypeName() {
        return "Резюме";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

//    public SeekerProfile getSeekerProfile() {
//        return seekerProfile;
//    }
//
//    public void setSeekerProfile(SeekerProfile seekerProfile) {
//        this.seekerProfile = seekerProfile;
//    }
}
