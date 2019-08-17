package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository("resumeDAO")
public class ResumeDAO extends AbstractDAO<Resume> {

    public Page<Resume> getAllResumes(int limit, int page) {
        page = (page == 0) ? page : --page;

        Query query = (Query) entityManager.createQuery("select r from Resume r", Resume.class);

        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));

        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        return new ResumePageDTO(resumes, totalPages);
    }
}
