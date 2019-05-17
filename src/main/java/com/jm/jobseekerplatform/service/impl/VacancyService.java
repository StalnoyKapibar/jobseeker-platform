package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.VacancyDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.AbstractService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("vacancyService")
@Transactional
public class VacancyService extends AbstractService<Vacancy> {

    @Autowired
    private VacancyDAO dao;

    public Set<Vacancy> getByTags(Set<Tag> tags, int limit) {
        return dao.getByTags(tags, limit);
    }

    public Map<Tag, List<Vacancy>> getMapByTags(Set<Tag> tags, int limit) {
        List<Vacancy> vacancyList = dao.getAll();
        Map<Tag, List<Vacancy>> vacancyMap = tags.stream()
                .map(tag -> new Pair<Tag, List<Vacancy>>(tag,
                        vacancyList.stream()
                                .filter(vacancy -> vacancy.getTags().contains(tag))
                                .limit(limit)
                                .collect(Collectors.toList())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return vacancyMap;
    }
}