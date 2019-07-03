package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.VacancyDaoI;
import com.jm.jobseekerplatform.dao.impl.VacancyDAO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("vacancyService")
@Transactional
public class VacancyService extends AbstractService<Vacancy> {

    @Autowired
    private VacancyDAO dao;

    @Autowired
    private TagService tagService;

    @Autowired
    private PointService pointService;

    @Autowired
    private VacancyDaoI vacancyDaoI;

    private Pattern pattern;
    private Matcher matcher;

    public Page<Vacancy> findAll(Pageable pageable) {
        return vacancyDaoI.findAll(pageable);
    }
    public Page<Vacancy> findAllByTags(Set<Tag> tags, Pageable pageable) {
        return vacancyDaoI.findAllByTags(tags,pageable);
    }

    public Set<Vacancy> getByTags(Set<Tag> tags, int limit) {
        return dao.getByTags(tags, limit);
    }

    public void blockPermanently(Vacancy vacancy) {
        vacancy.setState(State.BLOCK_PERMANENT);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public void blockTemporary(Vacancy vacancy, int periodInDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        Date expiryBlockDate = calendar.getTime();

        vacancy.setState(State.BLOCK_TEMPORARY);
        vacancy.setExpiryBlock(expiryBlockDate);
        dao.update(vacancy);
    }

    public void blockOwn(Vacancy vacancy) {
        vacancy.setState(State.BLOCK_OWN);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public void unblock(Vacancy vacancy) {
        vacancy.setState(State.ACCESS);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public int deletePermanentBlockVacancies() {
        return dao.deletePermanentBlockVacancies();
    }

    public int deleteExpiryBlockVacancies() {
        return dao.deleteExpiryBlockVacancies();
    }

    public boolean validateVacancy(Vacancy vacancy) {
        String headline_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        String city_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        boolean isCorrect;

        if (vacancy.getHeadline().isEmpty() || vacancy.getCity().isEmpty() || vacancy.getShortDescription().isEmpty() || vacancy.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Some fields is empty");
        }
        pattern = Pattern.compile(headline_pattern);
        matcher = pattern.matcher(vacancy.getHeadline());
        isCorrect = matcher.matches();

        pattern = Pattern.compile(city_pattern);
        matcher = pattern.matcher(vacancy.getCity());
        isCorrect &= matcher.matches();

        isCorrect &= vacancy.getTags().size() > 0;
        return isCorrect;
    }

    public void addNewVacancyFromRest(Vacancy vacancy){
        vacancy.setState(State.NO_ACCESS);
        Point point = vacancy.getCoordinates();
        pointService.add(point);
        vacancy.setCoordinates(point);
        Set<Tag> tags = vacancy.getTags();;
        Set<Tag> tagsNew = new HashSet<>();
        for (Tag tag:tags) {
            tagsNew.add(tagService.findByName(tag.getName()));
        }
        vacancy.setTags(tagsNew);
        dao.add(vacancy);
    }
}