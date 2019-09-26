package com.jm.jobseekerplatform.model.users;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class EmployerUser extends User {
    public EmployerUser() {
    }

    public EmployerUser(String email, char[] password, LocalDateTime date, EmployerProfile profile) {
        super(email, password, date);
    }

    @Override
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority("ROLE_EMPLOYER");
    }
}
