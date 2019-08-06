package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.VerificationToken;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository("verificationTokenDAO")
public class VerificationTokenDAO extends AbstractDAO<VerificationToken> {

    public VerificationToken findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM VerificationToken t WHERE t.token = :param", VerificationToken.class)
                .setParameter("param", token)
                .getSingleResult();
    }

    public VerificationToken findeByUserId(long userId){
        VerificationToken token = null;

        try{
            token = entityManager
                    .createQuery("FROM VerificationToken WHERE user_id = :param", VerificationToken.class)
                    .setParameter("param", userId)
                    .getSingleResult();
        } catch (NoResultException e){
            e.getStackTrace();
        }
        return token;

    }
}
