package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.EmployerReviewsDAO;
import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("employerReviewsService")
@Transactional
public class EmployerReviewsService extends AbstractService<EmployerReviews> {

    @Autowired
    private EmployerReviewsDAO employerReviewsDAO;

    @Override
    public void deleteById(Long reviewId) {
        employerReviewsDAO.deleteById(reviewId);
    }
}
