package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class SeekerUser extends User{
    public SeekerUser() {
    }

    public SeekerUser(String email, char[] password, LocalDateTime date) {
        super(email, password, date);
    }

    @Override
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_SEEKER");
    }
}
