package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface CommentDaoI extends JpaRepository<Comment, Long> {

    @Query("SELECT distinct c FROM Comment c JOIN c.news cn where cn=:currentNews")
    Page<Comment> getAllCommentsForNews(News currentNews, Pageable pageable);

}
