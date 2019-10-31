package com.jm.jobseekerplatform.dao.interfaces.tokens;

import com.jm.jobseekerplatform.dao.AbstractDao;
import com.jm.jobseekerplatform.model.tokens.BaseToken;


public class BaseTokenDao<T extends BaseToken> extends AbstractDao<T> {

    public T findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM " + clazz.getName() + " t WHERE t.token = :param", clazz)
                .setParameter("param", token)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    public boolean existsTokenByUserId(Long userId) {
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
