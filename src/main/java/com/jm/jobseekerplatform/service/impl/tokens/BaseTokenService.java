package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dao.impl.tokens.BaseTokenDAO;
import com.jm.jobseekerplatform.model.tokens.BaseToken;
import com.jm.jobseekerplatform.model.tokens.PasswordResetToken;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public abstract class BaseTokenService<T extends BaseToken > extends AbstractService<T> {

    private AbstractDAO<T > dao;

    public Date calculateExpiryDate() {
        int periodInDays = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        return calendar.getTime();
    }

//    public void createToken(String token, User user) {
//        BaseToken baseToken = new BaseToken (token, user, calculateExpiryDate());
//       dao.add((T) baseToken);
//    }

    public T findByToken(String token) {
        return dao.findByToken(token);
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
