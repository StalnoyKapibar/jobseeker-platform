package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    @Autowired
    private TagService tagService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public List<Tag> getVerified() {
        List<Tag> tags = tagService.getVerified();
        return tags;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {

        tagService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/change_verified/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> changeVerified(@PathVariable Long id) {

        Tag findedTag = tagService.getById(id);
        findedTag.setVerified(!findedTag.getVerified());
        tagService.update(findedTag);
        return ResponseEntity.ok(true);
    }
}
