package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    @Autowired
    private TagService tagService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @RequestMapping("/seeker/{seeker_id}")
    public Set<Tag> getSeekerTags(@PathVariable("seeker_id") Long seekerId) {
        return seekerProfileService.getById(seekerId).getTags();
    }
}
