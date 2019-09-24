package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.model.*;
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.util.stream.Collectors;


@Service("resumeService")
@Transactional
public class ResumeService extends AbstractService<Resume> {

    @Autowired
    private ResumeDAO dao;

    @Autowired
    private TagService tagService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CityService cityService;

    private Pattern pattern;
    private Matcher matcher;

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

    public void addResume(Resume resume) {
        Point point = resume.getCoordinates();
        pointService.add(point);
        Set<Tag> matchedTags = tagService.matchTagsByName(resume.getTags());
        City city = cityService.checkCityOrAdd(resume.getCity().getName(), point);
        resume.setTags(matchedTags);
        resume.setCoordinates(point);
        resume.setCity(city);
        dao.add(resume);
    }

    public void updateResume(Resume resume) {
        Point point = resume.getCoordinates();
        Set<Tag> matchedTags = tagService.matchTagsByName(resume.getTags());
        City city = cityService.checkCityOrAdd(resume.getCity().getName(), point);
        pointService.add(point);
        resume.setCoordinates(point);
        resume.setTags(matchedTags);
        resume.setCity(city);
        dao.update(resume);
    }

    public boolean validateResume(Resume resume) {
        String headline_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        String city_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        boolean isCorrect;
        if (resume.getHeadline().isEmpty() || resume.getCity().getName().isEmpty()) {
            throw new IllegalArgumentException("Some fields are empty");
        }
        patternHeadline = Pattern.compile(headline_pattern);
        patternCity = Pattern.compile(city_pattern);
        matcher = patternHeadline.matcher(resume.getHeadline());
        isCorrect = matcher.matches();
        matcher = patternCity.matcher(resume.getCity().getName());
        isCorrect &= matcher.matches();
        isCorrect &= resume.getTags().size() > 0;
        return isCorrect;
    }

    public void deleteByResumeId(Long id) {
        dao.deleteResumeById(id);
    }

    public Page<Resume> getPagableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(Map<String, Object> queryParamsMap,
                                                                                   int pageNumber, int pageSize) {
        return dao.getPagableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(queryParamsMap, pageNumber, pageSize);
 }
}
