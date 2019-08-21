package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository("resumeDAO")
public class ResumeDAO extends AbstractDAO<Resume> {

    public Page<Resume> getAllResumes(int limit, int page) {
        page = (page == 0) ? page : --page;

        Query query = entityManager.createQuery("select r from Resume r", Resume.class);

        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));

        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
        Query query = entityManager.createQuery("select v.id, count(v.id) as c from Resume v join v.tags t where t in (:tags) group by (v.id) order by c desc")
                .setParameter("tags", tags);
        return getResumes(query, tags, limit, page);
    }

    private Page<Resume> getResumes(Query query, Set<Tag> tags, int limit, int page) {
        long totalElements = (long) entityManager
                .createQuery("select count(distinct v) from Resume v join v.tags t where t in (:tags)")
                .setParameter("tags", tags)
                .getSingleResult();

        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));

        List ids = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
        List<Long> resumeIds = new ArrayList<>();
        for (Object resume : ids) {
            resumeIds.add((Long) ((Object[]) resume)[0]);
        }

        List<Resume> resumeList = entityManager.createQuery("select v from Resume v where v.id in (:ids)")
                .setParameter("ids", resumeIds)
                .getResultList();

        List<Resume> resumes = new ArrayList<>();
        for (Long id : resumeIds) {
            for (Resume r : resumeList) {
                if (r.getId().equals(id)) {
                    resumes.add(r);
                }
            }
        }
        return new ResumePageDTO(resumes, totalPages);
    }
}
