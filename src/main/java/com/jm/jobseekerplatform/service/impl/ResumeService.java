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

    public Page<Resume> getFilterQuery(Map<String, Object> map) {

        String query = "select r from Resume r ";
        String queryCount = "select count(distinct r)  from Resume r ";
        StringBuilder whereQuery = new StringBuilder(query);
        StringBuilder whereQueryCount = new StringBuilder(queryCount);
        if (map.containsKey("tagFls")) {
            whereQuery.append("join r.tags t WHERE r.id <> 0");
            whereQueryCount.append("join r.tags t WHERE r.id <> 0");
        } else {
            whereQuery.append("WHERE r.id <> 0");
            whereQueryCount.append("WHERE r.id <> 0");
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case "city":
                    whereQuery.append(" AND r.city.name = '"+entry.getValue()+"'");
                    whereQueryCount.append(" AND r.city.name = '"+entry.getValue()+"'");
                    break;
                case "salFrom":
                    whereQuery.append(" AND r.salaryMin >= '"+entry.getValue()+"'");
                    whereQueryCount.append(" AND r.salaryMin >= '"+entry.getValue()+"'");
                    break;
                case "salTo":
                    whereQuery.append(" AND r.salaryMax <= '"+entry.getValue()+"'");
                    whereQueryCount.append(" AND r.salaryMax <= '"+entry.getValue()+"'");
                    break;
                case "tagFls":
                    String tagsForQuery ="";
                    List<Long> tags = (List<Long>)entry.getValue();
                    if (tags.size() == 1) {
                        tagsForQuery = String.valueOf(tags.get(0));
                    } else {
                        for (int i = 0; i < tags.size(); i++) {
                            if (i == tags.size() - 1) {
                                tagsForQuery += String.valueOf(tags.get(i));
                            } else {
                                tagsForQuery += String.valueOf(tags.get(i));
                                tagsForQuery += ", ";
                            }
                        }
                    }
                    whereQuery.append(" AND t in ("+tagsForQuery+") group by (r.id)");
                    whereQueryCount.append(" AND t in ("+tagsForQuery+") group by (r.id)");
            }
        }
        return dao.getResumeByFilter(whereQuery.toString(),10,0, whereQueryCount.toString());


    }

}
