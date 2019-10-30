package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.ResumeDaoI;
import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("resumeService")
@Transactional
public class ResumeService extends AbstractService<Resume> {

    @Autowired
    private ResumeDAO dao;

    @Autowired
    private TagService tagService;

    @Autowired
    private ResumeDaoI resumeDaoI;

    @Autowired
    private PointService pointService;

    @Autowired
    private CityService cityService;

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
        Pattern pattern = Pattern.compile(headline_pattern);
        Matcher matcher = pattern.matcher(resume.getHeadline());
        isCorrect = matcher.matches();
        pattern = Pattern.compile(city_pattern);
        matcher = pattern.matcher(resume.getCity().getName());
        isCorrect &= matcher.matches();
        isCorrect &= resume.getTags().size() > 0;
        return isCorrect;
    }

    public void deleteByResumeId(Long id) {
        dao.deleteResumeById(id);
    }

    public Page<Resume> getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(Map<String,
            Object> queryParamsMap, int pageNumber, int pageSize) {
        return dao.getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(queryParamsMap,
                pageNumber, pageSize);
    }

    public List<Resume> getResumesByDatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return resumeDaoI.getResumesByDatePeriod(startDate, endDate);
    }

    public List<Resume> getAllResumesByTagName(String tagName) {
        return dao.getAllResumesByTagName(tagName);
    }
}
