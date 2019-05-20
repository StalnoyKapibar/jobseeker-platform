package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seekers")
public class Seeker extends User {

    @OneToOne(fetch = FetchType.EAGER)
    private SeekerProfile seekerProfile;

    public Seeker() {
    }

    public Seeker(String email, char[] password, UserRole authority, SeekerProfile seekerProfile) {
        super(email, password, authority);
        this.seekerProfile = seekerProfile;
    }

    @Override
    public String getUsername() {
        return seekerProfile.getName() + " " + seekerProfile.getSurname();
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }
}
