package com.jm.jobseekerplatform.model.tokens;

import com.jm.jobseekerplatform.model.users.User;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class VerificationToken extends BaseToken {

    public VerificationToken() {
    }

    public VerificationToken(String token, User user, Date expiryDate) {
        super(token, user, expiryDate);
    }

}
