package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class SeekerUser extends User<SeekerProfile> {
    public SeekerUser() {
    }

    public SeekerUser(String email, char[] password, LocalDateTime date, SeekerProfile profile) {
        super(email, password, date, profile);
    }

    @Override
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_SEEKER");
    }
}
