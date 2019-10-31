package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.CityDistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDistanceDao extends JpaRepository<CityDistance, Long> {

}
