package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDaoI extends JpaRepository<Comment, Long> {
}
