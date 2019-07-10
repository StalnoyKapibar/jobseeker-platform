package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.ProfileAdmin;
import com.jm.jobseekerplatform.model.UserRole;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminUser extends User<ProfileAdmin> {
    public AdminUser() {
    }

    public AdminUser(String email, char[] password, LocalDateTime date, UserRole authority, ProfileAdmin profile) {
        super(email, password, date, authority, profile);
    }
}
