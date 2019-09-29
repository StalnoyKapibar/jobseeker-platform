package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/api/comments/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllCommentsForNews(@PathVariable Long id) {
        List<Comment> commentList = new ArrayList<>();
        News news = newsService.getById(id);
          try{
              commentList = commentService.getAllCommentsForNews(news);

          }catch (Exception e){
              e.printStackTrace();
          }
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @RequestMapping(value ="/api/comments", params = {"id"}, method = RequestMethod.GET)
    public ResponseEntity<Comment> getComment(@RequestParam("id") Long id){
        Comment comment = commentService.getById(id);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }
   @RequestMapping(value = "/api/comments/update/{id}", method = RequestMethod.POST)
    public  ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment){
        Comment currentComment = commentService.getById(id);
        currentComment.setText(comment.getText());
        currentComment.setProfile(comment.getProfile());
        currentComment.setDateTime(comment.getDateTime());
        currentComment.setNews(comment.getNews());
        commentService.update(currentComment);
        return new ResponseEntity<Comment>(currentComment, HttpStatus.OK);
    }
}
