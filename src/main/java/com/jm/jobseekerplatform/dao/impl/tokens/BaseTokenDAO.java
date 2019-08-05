package com.jm.jobseekerplatform.dao.impl.tokens;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.tokens.BaseToken;


public class BaseTokenDAO<T extends BaseToken> extends AbstractDAO<T> {

    public T findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.token = :param", clazz)
                .setParameter("param", token)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    public boolean existsTokenByUserId(Long userId) {
//        return (boolean) entityManager
//                .createQuery("SELECT CASE WHEN EXISTS(SELECT t FROM " + clazz.getName() + " t WHERE t.user.id = :id) THEN true ELSE false END from " + clazz.getName())
//                .setParameter("id", userId)
//                .getSingleResult();
        return !entityManager
                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.user.id = :id", clazz)
                .setParameter("id", userId)
                .getResultList().isEmpty();
    }

    public T findTokenByUserId(Long userId) {
        return entityManager
                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.user.id = :id", clazz)
                .setParameter("id", userId)
                .getResultList()
                .stream().findFirst().orElse(null);
    }
}
