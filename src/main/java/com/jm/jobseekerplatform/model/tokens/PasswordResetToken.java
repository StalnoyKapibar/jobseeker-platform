package com.jm.jobseekerplatform.model.tokens;

import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PasswordResetToken extends BaseToken {

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user, Date expiryDate) {
        super(token, user, expiryDate);
    }
}
