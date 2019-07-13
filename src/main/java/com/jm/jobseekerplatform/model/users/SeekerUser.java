package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.UserRole;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class SeekerUser extends User<SeekerProfile> {
    public SeekerUser() {
    }

    public SeekerUser(String email, char[] password, LocalDateTime date, UserRole authority, SeekerProfile profile) {
        super(email, password, date, authority, profile);
    }
}
