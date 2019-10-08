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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/comments")
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllCommentsForNews(@PathVariable Long id) {
        List<Comment> commentList = new ArrayList<>();
        News news = newsService.getById(id);
        try {
            commentList = commentService.getAllCommentsForNews(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @RequestMapping(params = {"id"}, method = RequestMethod.GET)
    public ResponseEntity<Comment> getComment(@RequestParam("id") Long id) {
        Comment comment = commentService.getById(id);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", params = {"id", "text"},
            method = RequestMethod.PUT)
    public ResponseEntity<Comment> updateComment(@RequestParam("id") Long id,
                                                 @RequestParam("text") String text) {
        Comment comment = commentService.getById(id);
        comment.setText(text);
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        comment.setDateTime(dateTime);
        commentService.update(comment);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", params = {"id"}, method = RequestMethod.DELETE)
    public ResponseEntity<Comment> deleteComment(@RequestParam("id") Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<Comment>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/insert", params = {"newsId", "text"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> createComment(@RequestParam("newsId") Long id,
                                              @RequestParam("text") String text,
                                              Authentication authentication) {
        News news = newsService.getById(id);
        Profile profile = ((User)authentication.getPrincipal()).getProfile();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Comment comment = new Comment(text, news, profile, dateTime);
        commentService.add(comment);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
