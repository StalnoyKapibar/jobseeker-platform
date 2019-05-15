package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seekers")
public class Seeker extends User{

    @OneToOne(fetch = FetchType.EAGER)
    private SeekerProfile seekerProfile;

    public Seeker() {
    }

    public Seeker(String name, String surname, char[] password, String email, UserRole authority, SeekerProfile seekerProfile) {
        super(name, surname, password, email, authority);
        this.seekerProfile = seekerProfile;
    }
/*public Seeker(String login, char[] password, String email, UserRole authority, SeekerProfile seekerProfile) {
        super(login, password, email, authority);
        this.seekerProfile = seekerProfile;
    }*/

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }
}
