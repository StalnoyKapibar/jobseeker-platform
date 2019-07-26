package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.PasswordResetToken;
import org.springframework.stereotype.Repository;

@Repository("passwordResetTokenDao")
public class PasswordResetTokenDao extends AbstractDAO<PasswordResetToken> {

    public PasswordResetToken findByToken(String token) {
        return entityManager
                .createQuery("SELECT t FROM PasswordResetToken t WHERE t.token = :param", PasswordResetToken.class)
                .setParameter("param", token)
                .getSingleResult();
    }
}
