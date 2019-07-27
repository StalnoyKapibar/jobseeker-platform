package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.PasswordResetTokenDao;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.model.PasswordResetToken;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;

@Service("passwordResetTokenService")
@Transactional
public class PasswordResetTokenService extends AbstractService<PasswordResetToken> {

    @Autowired
    private PasswordResetTokenDao dao;

    private Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
    }

    public void createPasswordResetToken(String token, User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, calculateExpiryDate());
        dao.add(passwordResetToken);
    }

    public PasswordResetToken findByToken(String token) {
        return dao.findByToken(token);
    }

    public boolean tokenIsNonExpired(PasswordResetToken token) {
        Calendar calendar = Calendar.getInstance();
        boolean isNonExpired = (token.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0;
        if (!isNonExpired) {
            dao.delete(token);
        }
        return isNonExpired;
    }

    public void completeRecovery(PasswordResetToken token) {
        dao.delete(token);
    }
}
