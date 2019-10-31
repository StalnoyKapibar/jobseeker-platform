package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDao extends JpaRepository<City, Long> {

    City findCityByName(String name);
}
