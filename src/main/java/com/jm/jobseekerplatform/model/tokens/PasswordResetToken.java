package com.jm.jobseekerplatform.model.tokens;

import com.jm.jobseekerplatform.model.users.User;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class PasswordResetToken extends BaseToken {

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user, Date expiryDate) {
        super(token, user, expiryDate);
    }
}
