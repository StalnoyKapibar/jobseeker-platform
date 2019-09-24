package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/news", params = {"newsId"}, method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllCommentsForThisNews(@RequestParam Long newsId) {
        News news = newsService.getById(newsId);
        List<Comment> comments = news.getComments();
        if(comments.isEmpty()){
            return new ResponseEntity<List<Comment>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
    }
    @RequestMapping(value = "/news/{newsId}/insert", method = RequestMethod.POST)
    public ResponseEntity<Void> addCommentForNews(@PathVariable Long newsId,@RequestBody Comment comment){
        News news = newsService.getById(newsId);
        List<Comment>comments = news.getComments();
        comments.add(comment);
        news.setComments(comments);
        newsService.update(news);
        commentService.add(comment);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
