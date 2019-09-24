package com.jm.jobseekerplatform.service.impl.comments;

import com.jm.jobseekerplatform.dao.CommentRepository;
import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public void add(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List getAllComments() {
        return (List) commentRepository.findAll();
    }
}
