package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.CommentDTO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/comments")
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/{id}", params = {"pageNumber"}, method = RequestMethod.GET)
    public ResponseEntity<CommentDTO> getAllCommentsForNews(@PathVariable Long id,
                                                            @RequestParam("pageNumber") int pageNumber) {
        return new ResponseEntity<>(commentService.getAllCommentsForNews(
                newsService.getById(id), PageRequest.of(pageNumber, 3)), HttpStatus.OK);
    }

    @RequestMapping(params = {"id"}, method = RequestMethod.GET)
    public ResponseEntity<Comment> getComment(@RequestParam("id") Long id) {
        Comment comment = commentService.getById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", params = {"id", "text"},
            method = RequestMethod.PUT)
    public ResponseEntity<Comment> updateComment(@RequestParam("id") Long id,
                                                 @RequestParam("text") String text) {
        Comment comment = commentService.getById(id);
        comment.setText(text);
        commentService.update(comment);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", params = {"id"}, method = RequestMethod.DELETE)
    public ResponseEntity<Comment> deleteComment(@RequestParam("id") Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/insert", params = {"newsId", "text"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> createComment(@RequestParam("newsId") Long id,
                                              @RequestParam("text") String text,
                                              Authentication authentication) {
        News news = newsService.getById(id);
        Profile profile = ((User) authentication.getPrincipal()).getProfile();
        Comment comment = new Comment(text, news, profile, LocalDateTime.now());
        commentService.add(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
