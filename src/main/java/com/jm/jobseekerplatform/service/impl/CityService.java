package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AbstractService<City> {

    @Autowired
    private CityDAO cityDAO;

}
