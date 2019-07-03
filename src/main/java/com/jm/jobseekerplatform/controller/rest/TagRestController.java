package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<List<Tag>> getSearchTags(@RequestBody String param) {
        char[] chars = param.toCharArray();
        List<Tag> tags = tagService.getBySearchParam(String.valueOf(chars,1,chars.length-2));
        return new ResponseEntity<>(tags, HttpStatus.OK) ;
    }
}
