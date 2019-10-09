package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.EmployerReviews;
import org.springframework.stereotype.Repository;

@Repository("employerReviewsDAO")
public class EmployerReviewsDAO extends AbstractDAO<EmployerReviews> {
    @Override
    public void deleteById(Long reviewId) {
        entityManager.createQuery("delete from EmployerReviews where id =: reviewId")
                .setParameter("reviewId", reviewId)
                .executeUpdate();
    }
}
