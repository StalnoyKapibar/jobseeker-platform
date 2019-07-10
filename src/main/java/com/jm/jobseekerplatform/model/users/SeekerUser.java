package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
import com.jm.jobseekerplatform.model.UserRole;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class SeekerUser extends User<ProfileSeeker> {
    public SeekerUser() {
    }

    public SeekerUser(String email, char[] password, LocalDateTime date, UserRole authority, ProfileSeeker profile) {
        super(email, password, date, authority, profile);
    }
}
