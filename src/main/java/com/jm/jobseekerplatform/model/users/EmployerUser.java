package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
import com.jm.jobseekerplatform.model.UserRole;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class EmployerUser extends User<ProfileEmployer> {
    public EmployerUser() {
    }

    public EmployerUser(String email, char[] password, LocalDateTime date, UserRole authority, ProfileEmployer profile) {
        super(email, password, date, authority, profile);
    }
}
