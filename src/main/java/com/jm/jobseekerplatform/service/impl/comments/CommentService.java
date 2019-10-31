package com.jm.jobseekerplatform.service.impl.comments;

import com.jm.jobseekerplatform.dao.CommentDaoI;
import com.jm.jobseekerplatform.dto.CommentDTO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;

@Service("commentService")
@Transactional
public class CommentService extends AbstractService<Comment> {

    @Autowired
    private CommentDaoI commentDaoI;

    public CommentDTO getAllCommentsForNews(News currentNews, Pageable pageable){
        Page<Comment> page = commentDaoI.getAllCommentsForNews(currentNews, pageable);
        return new CommentDTO(page.getContent(), pageable.getPageNumber(), page.getTotalPages());
    }
}
