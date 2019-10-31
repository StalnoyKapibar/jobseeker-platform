package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.CityDao;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

@Service
public class CityService extends AbstractService<City> {

    @Autowired
    private CityDao cityDao;

    @Autowired
    private PointService pointService;

    @Autowired
    private CityDistanceService cityDistanceService;

    public City checkCityOrGetNearest(String currentCity, Point currentPoint) {
        City city = cityDao.findCityByName(currentCity);
        if (city!=null) {
            return city;
        } else {
            TreeMap<Float, City> sortedCity = new TreeMap<>();
            List<City> cityList = cityDao.findAll();
            for (City c : cityList) {
                float distance = pointService.getDistance(currentPoint, c.getPoint());
                sortedCity.put(distance, c);
            }
            return sortedCity.size() > 0 ? sortedCity.firstEntry().getValue() : new City(); // or null?
        }
    }

    public City checkCityOrAdd(String currentCity, Point currentPoint) {
        City city = cityDao.findCityByName(currentCity);
        if (city!=null) {
            return city;
        } else {
            return initCity(currentCity, currentPoint);
        }
    }

    public City initCity(String cityName, Point point) {
        City city = new City(cityName, point);
        cityDao.save(city);
        return cityDistanceService.initCityDistances(city);
    }

    public City getCityByName(String cityName){
        return cityDao.findCityByName(cityName);
    }

    public List<City> getAllCities() {
        return cityDao.findAll();
    }
}
