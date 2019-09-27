package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("seekerProfileDAO")
public class SeekerProfileDAO extends AbstractDAO<SeekerProfile> {

    public List<SeekerProfile> getAllSeekersById(List<Long> id) {
        return entityManager.createQuery("from SeekerProfile where id IN (:paramId)")
				.setParameter("paramId", id)
				.getResultList();
    }

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {
        Set<SeekerProfile> seekerProfiles = new HashSet<>();
        seekerProfiles.addAll(entityManager
                .createQuery("SELECT v FROM SeekerProfile v JOIN v.tags t WHERE t IN (:param)", SeekerProfile.class)
                .setParameter("param", tags)
                .getResultList());
        return seekerProfiles;
    }

    public SeekerProfile getByResume(Resume resume) {
        long id = resume.getId();
        return entityManager.createQuery("SELECT v FROM SeekerProfile v JOIN v.resumes r WHERE r.id =:param", SeekerProfile.class)
                .setParameter("param", id)
                .getSingleResult();
    }

}
