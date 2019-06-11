package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.VerificationTokenDAO;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.VerificationToken;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends AbstractService<VerificationToken> {

    @Autowired
    private VerificationTokenDAO dao;

    private Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
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
