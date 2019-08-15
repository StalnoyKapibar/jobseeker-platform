package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("seekerProfileDAO")
public class SeekerProfileDAO extends AbstractDAO<SeekerProfile> {

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {
        Set<SeekerProfile> seekerProfiles = new HashSet<>();
        seekerProfiles.addAll(entityManager
                .createQuery("SELECT v FROM SeekerProfile v JOIN v.tags t WHERE t IN (:param)", SeekerProfile.class)
                .setParameter("param", tags)
                .getResultList());
        return seekerProfiles;
    }
}
