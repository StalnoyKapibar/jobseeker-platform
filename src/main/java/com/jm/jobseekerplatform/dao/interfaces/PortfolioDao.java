package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioDao extends JpaRepository<Portfolio, Long> {
}
