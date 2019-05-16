package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository("verificationTokenDAO")
public class VerificationTokenDAO extends AbstractDAO<VerificationToken> {

    public VerificationToken findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM VerificationToken t WHERE t.token = :param", VerificationToken.class)
                .setParameter("param", token)
                .getSingleResult();
    }
}
