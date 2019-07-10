package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.UserRole;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class EmployerUser extends User<EmployerProfile> {
    public EmployerUser() {
    }

    public EmployerUser(String email, char[] password, LocalDateTime date, UserRole authority, EmployerProfile profile) {
        super(email, password, date, authority, profile);
    }
}
