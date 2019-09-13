package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository("resumeDAO")
public class ResumeDAO extends AbstractDAO<Resume> {

    public Page<Resume> getAllResumes(int limit, int page) {
        page = (page == 0) ? page : --page;
        Query query = entityManager.createQuery("select r from Resume r", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumeByFilter(String q, int limit, int page, String qCount) {
        page = (page == 0) ? page : --page;
        Query query = entityManager.createQuery(q, Resume.class);
        long totalElements = (long) entityManager.createQuery(qCount).getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
        Query query = entityManager.createQuery("select r from Resume r join r.tags t where t in (:tags) group by (r.id)", Resume.class)
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
                .createQuery("select r from Resume r join r.city c join CityDistance cd on c=cd.to where cd.from.name=:city order by cd.distance", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setParameter("city", city)
                .setFirstResult(page * limit)
                .setMaxResults(limit)
                .getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    private int getTotalPages(long totalElements, int limit) {
        return (int) (Math.ceil((double) totalElements / (double) limit));
    }

    public void deleteResumeById(Long id) {
        entityManager.createNativeQuery("delete from profile_resumes where profile_resumes.resumes_id = :id").setParameter("id", id).executeUpdate();
        entityManager.createQuery("delete from Resume where id = :id").setParameter("id", id).executeUpdate();
    }
}
