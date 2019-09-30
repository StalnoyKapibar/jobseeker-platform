package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

    @RequestMapping(params = {"id"}, method = RequestMethod.GET)
    public ResponseEntity<Comment> getComment(@RequestParam("id") Long id){
        Comment comment = commentService.getById(id);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }


    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public  ResponseEntity<Comment> updateComment(@RequestBody Comment comment){
        Comment currentComment = commentService.getById(comment.getId());
        currentComment.setText(comment.getText());
        currentComment.setDateTime(comment.getDateTime());
        currentComment.setNews(comment.getNews());
        commentService.update(currentComment);
        return new ResponseEntity<Comment>(currentComment, HttpStatus.OK);
    }
   /*@RequestMapping(value = "/update", params = {"id", "text", "dateTime"},method = RequestMethod.PUT)
    public  ResponseEntity<Comment> updateComment(@RequestParam("id") Long id, @RequestParam("text") String text, @RequestParam("dateTime") String dateTime){
        Comment currentComment = commentService.getById(id);
        return new ResponseEntity<Comment>(currentComment, HttpStatus.OK);
    }*/
}
