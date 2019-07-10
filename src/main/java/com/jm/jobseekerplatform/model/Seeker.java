package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Seeker extends User<SeekerProfile> {
    public Seeker() {
    }

    public Seeker(String email, char[] password, LocalDateTime date, UserRole authority, SeekerProfile profile) {
        super(email, password, date, authority, profile);
    }
}
