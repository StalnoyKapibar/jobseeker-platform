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

    public Map<Tag, List<Vacancy>> getMapVacancyByTags(Set<Tag> tags, int limit) {
        Map<Tag, List<Vacancy>> resultMap = dao.getListTuplesTagVacancy(tags).stream()
                .map(tuple -> new Pair<>(tuple.get(0, Tag.class),tuple.get(1, Vacancy.class)))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return  resultMap;
    }
}