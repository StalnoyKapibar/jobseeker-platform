package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.TagDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("tagService")
@Transactional
public class TagService extends AbstractService<Tag> {

    @Autowired
    private TagDAO dao;

    public Tag findByName(String name) {
        return dao.findByName(name);
    }
    /**
     * if tag not found, it means user (employer) added new tag
     * unverified while admin will not change it
     */
    public Tag getTag(String tagName) {
        Tag findedTag = null;
        try {
            findedTag = findByName(tagName);
        }
        catch (EmptyResultDataAccessException e) {

            Tag newTag = new Tag(tagName);
            /**
             * all new tags, when we creaded the vacancy, aren't verified
             */
            newTag.setVerified(false);

            add(newTag);
            findedTag = findByName(newTag.getName());
        }
        return findedTag;
    }

    public List<Tag> getVerified() {
        boolean verified = true;
        return dao.getVerified(verified);
    }

    public List<Tag> getUnverified() {
        boolean verified = false;
        return dao.getVerified(verified);
    }
}
