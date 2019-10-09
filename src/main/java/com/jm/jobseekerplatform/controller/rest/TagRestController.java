package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.*;

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

    @RolesAllowed({"ROLE_SEEKER", "ROLE_EMPLOYER", "ROLE_ADMIN"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @RequestMapping(value = "/change_verified/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> changeVerified(@PathVariable Long id) {

        Tag findedTag = tagService.getById(id);
        findedTag.setVerified(!findedTag.getVerified());
        tagService.update(findedTag);
        return ResponseEntity.ok(true);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @RequestMapping(value = "/createNewTagController", method = RequestMethod.POST)
    public void createNewTagController(@RequestBody Tag tag) {
        tagService.createNewTagService(tag);
    }

}
