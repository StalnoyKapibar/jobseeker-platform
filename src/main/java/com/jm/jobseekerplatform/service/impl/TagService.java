package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.TagDao;
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
    private TagDao tagDao;

    public Tag findByName(String name) {
        return tagDao.findByName(name);
    }

    public List<Tag> getBySearchParam(String param) {
        return tagDao.getBySearchParam(param);
    }

    /**
     * if tag not found, it means user (employer) added new tag
     * unverified while admin will not change it
     */
    public Set<Tag> createTags(Set<Tag> formTags, List<Tag> foundedTags) {

        boolean verified = false;
        Set<Tag> createdTags = new HashSet<>();
        for (Tag formTag : formTags) {
            if (!foundedTags.contains(formTag)) {
                Tag newTag = new Tag(formTag.getName(), verified);
                tagDao.save(newTag);
                createdTags.add(newTag);
            }
        }
        return createdTags;
    }

    /**
     * find or create tags by Name
     * @param inputTagsName contains new enitity Tag with filled field Name
     * @return found or created tags
     */
    public Set<Tag> matchTagsByName(Set<Tag> inputTagsName) {

        Set<Tag> result = new HashSet<>();

        List<Tag> foundedTags = getTagsByNames(inputTagsName);
        Set<Tag> createdTags = createTags(inputTagsName, foundedTags);

        result.addAll(foundedTags);
        result.addAll(createdTags);

        return result;
    }

    private List<Tag> getTagsByNames(Set<Tag> inputTagsName) {

        // convert set Tags to set String
        Set<String> tagsName = new HashSet<>();
        inputTagsName.forEach(curTag -> tagsName.add(curTag.getName()));
        // find set tags name in db
        List<Tag> findedTags = tagDao.findAllByName(tagsName);

        return findedTags;
    }

    public Set<Tag> getTagsByStringNames(Set<String> inputTagsName){
        return new HashSet<>(tagDao.findAllByName(inputTagsName));
    }

    public List<Tag> getVerified() {
        boolean verified = true;
        return tagDao.findAllByVerified(verified);
    }

    public List<Tag> getUnverified() {
        boolean verified = false;
        return tagDao.findAllByVerified(verified);
    }

    public List<Tag> getSortedAll(){
        List<Tag> all = getUnverified();
        all.addAll(getVerified());
        return all;
    }

    @Override
    public void deleteById(Long id) {
        tagDao.deleteById(id);
    }

    public void createNewTagService(Tag tag) {
        tagDao.save(tag);
    }

}
