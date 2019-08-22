package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
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

    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
        Query query = entityManager.createQuery("select v from Resume v join v.tags t where t in (:tags) group by (v.id)", Resume.class)
                .setParameter("tags", tags);
        long totalElements = (long) entityManager
                .createQuery("select count(distinct v) from Resume v join v.tags t where t in (:tags)")
                .setParameter("tags", tags)
                .getSingleResult();

        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumeList = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        return new ResumePageDTO(resumeList, totalPages);
    }

    private int getTotalPages(long totalElements, int limit) {
        return (int) (Math.ceil((double) totalElements / (double) limit));
    }
}
