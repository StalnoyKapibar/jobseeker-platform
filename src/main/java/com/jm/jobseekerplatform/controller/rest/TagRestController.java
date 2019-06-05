package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    @Autowired
    private TagService tagService;

    @RequestMapping("/")
    public List<Tag> getAllTags() {
        List<Tag> tags = tagService.getAll();
        return tags;
    }
}
