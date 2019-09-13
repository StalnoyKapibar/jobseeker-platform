package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.SeekerShowName;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("seekerTestDAO")
public class SeekerShowNameDAO extends AbstractDAO<SeekerShowName> {

    public List<SeekerShowName> getAllSeekerTestById(List<Long> id) {
        return entityManager.createQuery("from SeekerProfile " +
				"where id IN (:paramId)").setParameter("paramId", id).getResultList();
    }

}
