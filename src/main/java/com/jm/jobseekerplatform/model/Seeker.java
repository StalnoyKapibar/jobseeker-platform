package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "seekers")
public class Seeker extends User {

    public Seeker() {
    }

    public Seeker(String email, char[] password, LocalDateTime date, UserRole authority) {
        super(email, password, date, authority);
    }

//    @Override
//    public String getUsername() { //todo
//        return seekerProfile.getName() + " " + seekerProfile.getSurname();
//    }
}
