package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.reports.CommentReport;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import com.jm.jobseekerplatform.service.impl.reports.CommentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report/comments/")
public class CommentReportRestController {

    @Autowired
    private CommentReportService commentReportService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<CommentReport>> getAllCommentsReport(){
        List<CommentReport> commentReportList = commentReportService.getAll();
        return new ResponseEntity<>(commentReportList, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", params = {"id", "dateTime", "description"}, method = RequestMethod.POST)
    public ResponseEntity<Void> addCommentReport(@RequestParam("id") Long id,
                                                 @RequestParam("dateTime") String dateTime,
                                                 @RequestParam("description") String description,
                                                 Authentication authentication){
        Comment comment = commentService.getById(id);
        Profile author = ((User) authentication.getPrincipal()).getProfile();
        CommentReport commentReport = new CommentReport(dateTime, description, comment, author);
        commentReportService.add(commentReport);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
