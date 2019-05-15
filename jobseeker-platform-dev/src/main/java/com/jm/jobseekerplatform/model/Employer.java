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

    public Employer(String name, String surname, char[] password, String email, UserRole authority, EmployerProfile employerProfile) {
        super(name, surname, password, email, authority);
        this.employerProfile = employerProfile;
    }
    /* public Employer(String login, char[] password, String email, UserRole authority, EmployerProfile employerProfile) {
        super(login, password, email, authority);
        this.employerProfile = employerProfile;
    }*/

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }
}
