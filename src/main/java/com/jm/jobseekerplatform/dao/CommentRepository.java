package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CommentRepository extends CrudRepository<Comment, Long> {
}
