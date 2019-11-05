package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Reply;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.ReplyService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reply")
public class ReplyRestController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/add", params = {"newsId", "text", "commentId", "level"}, method = RequestMethod.POST)
    public ResponseEntity<Void> addReplyOnComment(@RequestParam("newsId") Long newsId,
                                                  @RequestParam("text") String text,
                                                  @RequestParam("commentId") Long commentId,
                                                  @RequestParam("level") Long level,
                                                  Authentication authentication) {
        Profile profile = ((User) authentication.getPrincipal()).getProfile();
        LocalDateTime dateTime = LocalDateTime.now();
        Reply reply  = new Reply(text, dateTime, profile);
        replyService.add(reply);
        Comment comment = commentService.getById(commentId);
        Set<Reply> replies = comment.getReplies();
        replies.add(reply);
        comment.setReplies(replies);
        commentService.update(comment);
        commentService.add(new Comment(text, newsService.getById(newsId), profile, dateTime, true, level));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Reply>> getAllRepliesForComment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(replyService.getAllRepliesForComment(commentService.getById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/insert", params = {"commentId", "text", "addressId", "level"}, method = RequestMethod.POST)
    public ResponseEntity<Void> addReplyOnReply(@RequestParam("commentId") Long id,
                                                @RequestParam("text") String text,
                                                @RequestParam("addressId") Long address,
                                                @RequestParam("level") Long level,
                                                Authentication authentication) {
        Comment comment = commentService.getById(id);
        Profile profile = ((User) authentication.getPrincipal()).getProfile();
        LocalDateTime dateTime = LocalDateTime.now();
        replyService.add(new Reply(text, dateTime, address, level, profile, comment));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(params = {"id"}, method = RequestMethod.GET)
    public ResponseEntity<Reply> getComment(@RequestParam("id") Long id) {
        Reply reply = replyService.getById(id);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", params = {"id"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteReply(@RequestParam("id") Long id) {
        List<Reply> replies = replyService.getAllRepliesByAddress(id);
        for(Reply r: replies){
            replyService.delete(r);
        }
        replyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/update", params = {"id", "text"},
            method = RequestMethod.PUT)
    public ResponseEntity<Reply> updateReply(@RequestParam("id") Long id,
                                             @RequestParam("text") String text) {
        Reply reply = replyService.getById(id);
        reply.setText(text);
        replyService.update(reply);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }*/
}
