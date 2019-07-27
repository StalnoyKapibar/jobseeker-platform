package com.jm.jobseekerplatform.model.tokens;

import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class VerificationToken extends BaseToken {

    public VerificationToken() {
    }

    public VerificationToken(String token, User user, Date expiryDate) {
        super(token, user, expiryDate);
    }

}
