package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {

    List<Comment> findAllByNews(News news);
}
