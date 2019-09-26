package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.AdminProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminUser extends User {
    public AdminUser() {
    }

    public AdminUser(String email, char[] password, LocalDateTime date, AdminProfile profile) {
        super(email, password, date);
    }

    @Override
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_ADMIN");
    }
}
