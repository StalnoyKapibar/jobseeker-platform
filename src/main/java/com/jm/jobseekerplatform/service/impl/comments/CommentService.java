package com.jm.jobseekerplatform.service.impl.comments;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commentService")
@Transactional
public class CommentService extends AbstractService<Comment> {

}
