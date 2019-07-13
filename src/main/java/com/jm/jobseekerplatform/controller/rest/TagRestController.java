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
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    @Autowired
    private TagService tagService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/seeker/{seeker_id}")
    public Set<Tag> getSeekerTags(@PathVariable("seeker_id") Long seekerId) {
        return seekerProfileService.getById(seekerId).getTags();
    }


    @RequestMapping("/")
    public List<Tag> getVerified() {
        List<Tag> tags = tagService.getVerified();
        return tags;
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
