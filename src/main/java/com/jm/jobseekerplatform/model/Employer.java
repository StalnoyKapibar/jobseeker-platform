package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employers")
public class Employer extends User {

    @OneToOne(fetch = FetchType.EAGER)
    private EmployerProfile employerProfile;

    public Employer() {
    }

    public Employer(String login, char[] password, UserRole authority, EmployerProfile employerProfile) {
        super(login, password, authority);
        this.employerProfile = employerProfile;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }
}
