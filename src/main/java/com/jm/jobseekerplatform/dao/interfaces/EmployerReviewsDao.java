package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.EmployerReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerReviewsDao extends JpaRepository<EmployerReviews, Long> {
}
