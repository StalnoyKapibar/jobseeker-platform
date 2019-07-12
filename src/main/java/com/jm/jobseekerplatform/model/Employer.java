package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Employer extends User<EmployerProfile> {
    public Employer() {
    }

    public Employer(String email, char[] password, LocalDateTime date, UserRole authority, EmployerProfile profile) {
        super(email, password, date, authority, profile);
    }
}
