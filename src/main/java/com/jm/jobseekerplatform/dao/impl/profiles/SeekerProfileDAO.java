package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("seekerProfileDAO")
public class SeekerProfileDAO extends AbstractDAO<ProfileSeeker> {

    public Set<ProfileSeeker> getByTags(Set<Tag> tags, int limit) {
        Set<ProfileSeeker> profileSeekers = new HashSet<>();
        profileSeekers.addAll(entityManager
                .createQuery("SELECT v FROM SeekerProfile v JOIN v.tags t WHERE t IN (:param)", ProfileSeeker.class) //todo check table SeekerProfile
                .setParameter("param", tags)
                .getResultList());
        return profileSeekers;
    }
}
