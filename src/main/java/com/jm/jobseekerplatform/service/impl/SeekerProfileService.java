package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.ProfileSeeker;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<ProfileSeeker> {

    @Autowired
    private SeekerProfileDAO dao;

    public Set<ProfileSeeker> getByTags(Set<Tag> tags, int limit) {

        return dao.getByTags(tags, limit);
    }
}
