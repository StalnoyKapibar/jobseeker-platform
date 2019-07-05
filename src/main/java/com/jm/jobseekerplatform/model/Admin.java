package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class Admin extends User<AdminProfile> {
    public Admin() {
    }

    public Admin(String email, char[] password, LocalDateTime date, UserRole authority, AdminProfile profile) {
        super(email, password, date, authority, profile);
    }
}
