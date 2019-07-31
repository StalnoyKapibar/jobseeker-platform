package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dao.impl.tokens.BaseTokenDAO;
import com.jm.jobseekerplatform.model.tokens.BaseToken;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public abstract class BaseTokenService<T extends BaseToken> extends AbstractService<T> {

    protected final BaseTokenDAO<T> baseTokenDAO;

    public BaseTokenService(BaseTokenDAO<T> baseTokenDAO) {
        this.baseTokenDAO =  baseTokenDAO;
    }

    public Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
    }

    public T findByToken(String token) {
        return baseTokenDAO.findByToken(token);
    }

    public boolean tokenIsNonExpired(T token) {
        Calendar calendar = Calendar.getInstance();
        boolean isNonExpired = (token.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0;
        if (!isNonExpired) {
            abstractDAO.delete(token);
        }
        return isNonExpired;
    }
}
