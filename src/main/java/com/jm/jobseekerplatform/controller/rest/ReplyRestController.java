package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Reply;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.ReplyService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reply")
public class ReplyRestController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/add", params = {"commentId", "text"}, method = RequestMethod.POST)
    public ResponseEntity<Void> addReplyOnComment(@RequestParam("commentId") Long id,
                                                  @RequestParam("text") String text,
                                                  Authentication authentication) {
        Comment comment = commentService.getById(id);
        Profile profile = ((User) authentication.getPrincipal()).getProfile();
        LocalDateTime dateTime = LocalDateTime.now();
        replyService.add(new Reply(text, dateTime, 1L, profile, comment));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Reply>> getAllRepliesForComment(@PathVariable("id") Long id){
        return new ResponseEntity<>(replyService.getAllRepliesForComment(commentService.getById(id)), HttpStatus.OK);
    }
}
