package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.*;

@Repository("resumeDAO")
public class ResumeDAO extends AbstractDAO<Resume> {

    @Autowired
    private SeekerProfileService seekerProfileService;

    public Page<Resume> getAllResumes(int limit, int page) {
        page = (page == 0) ? page : --page;
        Query query = entityManager.createQuery("select r from Resume r", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        List<SeekerProfile> seeker = seekerProfileService.getAllSeekersById(resumes);
        return new ResumePageDTO(resumes, totalPages, seeker);
    }

    public Page<Resume> getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(Map<String,
            Object> queryParamsMap, int pageNumber, int pageSize) {
        String query = "select r from Resume r ";
        String queryCount = "select count(distinct r)  from Resume r ";
        StringBuilder whereQuery = new StringBuilder(query);
        StringBuilder whereQueryCount = new StringBuilder(queryCount);
        if (queryParamsMap.containsKey("tagFls")) {
            whereQuery.append("join r.tags t WHERE r.id <> 0");
            whereQueryCount.append("join r.tags t WHERE r.id <> 0");
        } else {
            whereQuery.append("WHERE r.id <> 0");
            whereQueryCount.append("WHERE r.id <> 0");
        }
        for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
            switch (entry.getKey()) {
                case "city":
                    whereQuery.append(" AND r.city.name = '" + entry.getValue() + "'");
                    whereQueryCount.append(" AND r.city.name = '" + entry.getValue() + "'");
                    break;
                case "salFrom":
                    whereQuery.append(" AND r.salaryMin >= '" + entry.getValue() + "'");
                    whereQueryCount.append(" AND r.salaryMin >= '" + entry.getValue() + "'");
                    break;
                case "salTo":
                    whereQuery.append(" AND r.salaryMax <= '" + entry.getValue() + "'");
                    whereQueryCount.append(" AND r.salaryMax <= '" + entry.getValue() + "'");
                    break;
                case "tagFls":
                    String tagsForQuery = "";
                    List<Long> tags = (List<Long>) entry.getValue();
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
                    whereQuery.append(" AND t in (" + tagsForQuery + ") group by (r.id)");
                    whereQueryCount.append(" AND t in (" + tagsForQuery + ") group by (r.id)");
            }
        }
        pageNumber = (pageNumber == 0) ? pageNumber : --pageNumber;
        Query queryToHql = entityManager.createQuery(whereQuery.toString(), Resume.class);
        long totalElements = (long) entityManager.createQuery(whereQueryCount.toString()).getSingleResult();
        int totalPages = getTotalPages(totalElements, pageSize);
        List<Resume> resumes = queryToHql.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
        Query query = entityManager.createQuery("select r from Resume r join r.tags t where t" +
                " in (:tags) group by (r.id)", Resume.class)
                .setParameter("tags", tags);
        long totalElements = (long) entityManager
                .createQuery("select count(distinct r) from Resume r join r.tags t where t in (:tags)")
                .setParameter("tags", tags)
                .getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumesSortByCity(String city, int limit, int page) {
        page = (page == 0) ? page : --page;
        Query query = entityManager
                .createQuery("select r from Resume r join r.city c join CityDistance cd" +
                        " on c=cd.to where cd.from.name=:city order by cd.distance", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setParameter("city", city)
                .setFirstResult(page * limit)
                .setMaxResults(limit)
                .getResultList();
        List<SeekerProfile> seeker = seekerProfileService.getAllSeekersById(resumes);
        return new ResumePageDTO(resumes, totalPages, seeker);
    }

    private int getTotalPages(long totalElements, int limit) {
        return (int) (Math.ceil((double) totalElements / (double) limit));
    }


    public void deleteResumeById(Long id) {
        entityManager.createNativeQuery("delete from profile_resumes where profile_resumes.resumes_id = :id")
                .setParameter("id", id).executeUpdate();
        entityManager.createQuery("delete from Resume where id = :id").setParameter("id", id).executeUpdate();
    }

    public List<Resume> getAllResumesByTagName(String tagName) {
        Query query = entityManager.createQuery("SELECT distinct r FROM Resume r JOIN r.tags rt where rt.name = :name", Resume.class);
        query.setParameter("name", tagName);
        return query.getResultList();
    }
}
