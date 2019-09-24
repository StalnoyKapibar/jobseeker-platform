package com.jm.jobseekerplatform.service.impl.comments;

import com.jm.jobseekerplatform.model.comments.Comment;

import java.util.List;

public interface CommentService {
    void add(Comment comment);

    List getAllComments();
}
