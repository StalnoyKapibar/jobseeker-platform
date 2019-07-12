package com.jm.jobseekerplatform.controller.rest;

<<<<<<< HEAD
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
=======
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
>>>>>>> c4aab85a3b7b18a227459e49349585f8ff7fcbf5

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    @Autowired
    private TagService tagService;

    @Autowired
<<<<<<< HEAD
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @RequestMapping("/seeker/{seeker_id}")
    public Set<Tag> getSeekerTags(@PathVariable("seeker_id") Long seekerId) {
        return seekerProfileService.getById(seekerId).getTags();
=======
    private VacancyService vacancyService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public List<Tag> getVerified() {
        List<Tag> tags = tagService.getVerified();
        return tags;
>>>>>>> c4aab85a3b7b18a227459e49349585f8ff7fcbf5
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<List<Tag>> getSearchTags(@RequestBody String param) {
        char[] chars = param.toCharArray();
        List<Tag> tags = tagService.getBySearchParam(String.valueOf(chars, 1, chars.length - 2));
        return new ResponseEntity<>(tags, HttpStatus.OK);
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
