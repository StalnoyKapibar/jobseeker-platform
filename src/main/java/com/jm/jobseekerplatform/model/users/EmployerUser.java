package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class EmployerUser extends User<EmployerProfile> {
    public EmployerUser() {
    }

    public EmployerUser(String email, char[] password, LocalDateTime date, EmployerProfile profile) {
        super(email, password, date, profile);
    }

    @Override
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_EMPLOYER");
    }
}
