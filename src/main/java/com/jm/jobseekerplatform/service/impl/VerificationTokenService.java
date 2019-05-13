package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.VerificationTokenDAO;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.VerificationToken;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends AbstractService<VerificationToken> {

    @Autowired
    private VerificationTokenDAO dao;

    @Autowired
    private UserService userService;

    private Date calculateExpiryDate() {
        int expiryTimeInMinutes = 60 * 24;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

    public void createVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user, calculateExpiryDate());
        dao.add(verificationToken);
    }

    public VerificationToken findByToken(String token) {
        return dao.findByToken(token);
    }

    public boolean tokenIsNonExpired(VerificationToken token) {
        Calendar calendar = Calendar.getInstance();
        boolean isNonExpired = (token.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0;
        if (!isNonExpired) {
            dao.delete(token);
        }
        return isNonExpired;
    }

    public void completeRegistration(VerificationToken token) {
        token.getUser().setConfirm(true);
        dao.delete(token);
    }
}
