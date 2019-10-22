package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.SeekerStatusNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerStatusNewsDAOI extends JpaRepository<SeekerStatusNews, Long> {

}
