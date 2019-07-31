package com.jm.jobseekerplatform.dao.impl.tokens;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.tokens.BaseToken;



public  class BaseTokenDAO<T extends BaseToken> extends AbstractDAO<T> {

    public T findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.token = :param", clazz)
                .setParameter("param", token)
                .getSingleResult();
    }
//   TODO: проверка наличия токена у юзера
//    public T findByUserId(Long id) {
//        return entityManager
//                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.user = :id", clazz)
//                .setParameter("id", id)
//                .getSingleResult();
//    }
}
