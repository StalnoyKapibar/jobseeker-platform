package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.EmployerReviews;
import org.springframework.stereotype.Repository;

@Repository("employerReviewsDAO")
public class EmployerReviewsDAO extends AbstractDAO<EmployerReviews> {

    public EmployerReviews findBySeekerId(Long seekerId) {
        return entityManager.createQuery("select r from EmployerReviews as r where r.seekerProfile.id = :id", EmployerReviews.class).setMaxResults(1).setParameter("id", seekerId).getSingleResult();
        /*TODO*/
        /*remove maxResult*/
    }
}
