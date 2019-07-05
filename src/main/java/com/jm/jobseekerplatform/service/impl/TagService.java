package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.TagDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Tag> getBySearchParam(String param) {
        return dao.getBySearchParam(param);
    }
}
