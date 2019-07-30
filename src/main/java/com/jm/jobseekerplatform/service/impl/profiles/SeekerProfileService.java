package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<SeekerProfile> {

    @Autowired
    private SeekerProfileDAO dao;

    @Autowired
    private TagService tagService;

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {

        return dao.getByTags(tags, limit);
    }

    public Set<Tag> getNewTags(List<String> tags){
        List<Tag> tagsFromDB = tagService.getVerified();
        Set<Tag> newTags = new HashSet<>();
        for (Tag tag : tagsFromDB) {
            if (tags.contains(tag.getName())) {
                newTags.add(tag);
            }
        }
        return newTags;
    }
}
