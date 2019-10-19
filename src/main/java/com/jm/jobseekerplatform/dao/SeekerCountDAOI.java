package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.SeekerCountNewsViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerCountDAOI extends JpaRepository<SeekerCountNewsViews, Long> {

}
