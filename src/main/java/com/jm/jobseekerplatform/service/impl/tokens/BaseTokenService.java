package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.interfaces.tokens.BaseTokenDao;
import com.jm.jobseekerplatform.model.tokens.BaseToken;
import com.jm.jobseekerplatform.service.AbstractService;

import java.util.Calendar;
import java.util.Date;

public abstract class BaseTokenService<T extends BaseToken> extends AbstractService<T> {

    protected final BaseTokenDao<T> baseTokenDao;

    public BaseTokenService(BaseTokenDao<T> baseTokenDAO) {
        this.baseTokenDao = baseTokenDAO;
    }

    public Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
    }

    public T findByToken(String token) {
        return baseTokenDao.findByToken(token);
    }

    public T findTokenByUserId(Long userId) {
        return baseTokenDao.findTokenByUserId(userId);
    }

    public boolean existsTokenByUserId(Long userID) {
        return baseTokenDao.existsTokenByUserId(userID);
    }

    public boolean tokenIsNonExpired(T token) {
        Calendar calendar = Calendar.getInstance();
        boolean isNonExpired = (token.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0;
        if (!isNonExpired) {
            abstractDao.delete(token);
        }
        return isNonExpired;
    }
}
