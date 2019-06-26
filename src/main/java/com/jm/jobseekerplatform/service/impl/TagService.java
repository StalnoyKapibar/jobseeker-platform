package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.TagDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("tagService")
@Transactional
public class TagService extends AbstractService<Tag> {

    @Autowired
    private TagDAO dao;

    public Tag findByName(String name) {
        return dao.findByName(name);
    }

    public void getAndAddTag(Tag desiredTag, List<Tag> allTags, Set<Tag> result) {

        result.add(FindOrCreateTag(desiredTag, allTags));
    }

    /**
     * if tag not found, it means user (employer) added new tag
     * unverified while admin will not change it
     */
    public Tag FindOrCreateTag(Tag desiredTag, List<Tag> allTags) {

        if (allTags.contains(desiredTag)) {
            int i = allTags.indexOf(desiredTag);
            return allTags.get(i);
        }

        boolean verified = false;
        Tag newTag = new Tag(desiredTag.getName(), verified);
        dao.add(newTag);
        return newTag;
    }

    /**
     * find or create tags by Name
     * @param tagsName contains new enitity Tag with filled field Name
     * @return found or created tags
     */
    public Set<Tag> matchTagsByName(Set<Tag> tagsName) {

        Set<Tag> result = new HashSet<>();
        List<Tag> allExistingTags = dao.getAll();
        tagsName.forEach(currentTag -> getAndAddTag(currentTag, allExistingTags, result));

        return result;
    }

    public List<Tag> getVerified() {
        boolean verified = true;
        return dao.getVerified(verified);
    }

    public List<Tag> getUnverified() {
        boolean verified = false;
        return dao.getVerified(verified);
    }

    @Override
    public void deleteById(Long id) {
        dao.deleteById(id);
    }
}
