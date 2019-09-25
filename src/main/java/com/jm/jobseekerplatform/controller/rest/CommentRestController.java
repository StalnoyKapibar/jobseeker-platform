package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentRestController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/api/comments/", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllComments() {
          List<Comment> commentList = null;
          try{
              commentList = commentService.getAll();
          }catch (Exception e){
              e.printStackTrace();
          }
        if(commentList.isEmpty()){
            return new ResponseEntity<List<Comment>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Comment>>(commentList, HttpStatus.OK);
    }
}
