package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<SeekerProfile> {

    @Autowired
    private SeekerProfileDAO dao;

    public List<SeekerProfile> getAllSeekersById(List<Long> id) {
        return dao.getAllSeekersById(id);
    }

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {
        return dao.getByTags(tags, limit);
    }

}
