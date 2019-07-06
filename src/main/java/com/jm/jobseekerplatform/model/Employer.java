package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "employers")
public class Employer extends User {

    @OneToOne(fetch = FetchType.EAGER)
    private EmployerProfile employerProfile;

    public Employer() {
    }

    public Employer(String email, char[] password, LocalDateTime date, UserRole authority, EmployerProfile employerProfile) {
        super(email, password, date, authority);
        this.employerProfile = employerProfile;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }
}
