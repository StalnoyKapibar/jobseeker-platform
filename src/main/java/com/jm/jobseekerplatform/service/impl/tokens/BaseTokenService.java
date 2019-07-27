package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.impl.tokens.BaseTokenDAO;
import com.jm.jobseekerplatform.model.tokens.BaseToken;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class BaseTokenService<T extends BaseToken > extends AbstractService<T> {

    @Autowired
    BaseTokenDAO dao;

    private Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
    }

    public void createToken(String token, User user) {
        BaseToken baseToken = new BaseToken (token, user, calculateExpiryDate());
       dao.add((T) baseToken);
    }

    public T findByToken(String token) {
        return (T) dao.findByToken(token);
    }

    public boolean tokenIsNonExpired(BaseToken token) {
        Calendar calendar = Calendar.getInstance();
        boolean isNonExpired = (token.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0;
        if (!isNonExpired) {
            abstractDAO.delete((T) token);
        }
        return isNonExpired;
    }

}
