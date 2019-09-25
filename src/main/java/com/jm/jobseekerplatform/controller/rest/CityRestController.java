package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.service.impl.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cities")
public class CityRestController {

    @Autowired
    CityService cityService;

    @GetMapping("/")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }
}
