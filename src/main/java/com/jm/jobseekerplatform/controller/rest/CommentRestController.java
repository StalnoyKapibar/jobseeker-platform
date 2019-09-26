package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentRestController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/api/comments/", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> commentList = new ArrayList<>();
          try{
              Comment comment1 = commentService.getById(1L);
              Comment comment2 = commentService.getById(2L);
              commentList.add(comment1);
              commentList.add(comment2);

          }catch (Exception e){
              e.printStackTrace();
          }
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }
}
