package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.dao.impl.TagDAO;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service("resumeService")
@Transactional
public class ResumeService extends AbstractService<Resume> {

    @Autowired
    private ResumeDAO dao;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private TagDAO tagDAO;

    public Page<Resume> getAllResumes(int limit, int page) {
        return dao.getAllResumes(limit, page);
    }

    public Page<Resume> findAllByTags(Set<Tag> tags, Pageable pageable) {
        return dao.getResumesByTag(tags, pageable.getPageSize(), pageable.getPageNumber());
    }

    public Page<Resume> findResumesByPoint(String currentCity, Point point, int limit, int page) {
        String city = cityService.checkCityOrGetNearest(currentCity, point).getName();
        return dao.getResumesSortByCity(city, limit, page);
    }

    public void deleteByResumeId(Long id) {
        dao.deleteResumeById(id);
    }

    public Page<Resume> getResumeByFilter(Map<String, Object> map, int page, int limit) {
        return dao.getResumeByFilter(map, page, limit);
 }

}
