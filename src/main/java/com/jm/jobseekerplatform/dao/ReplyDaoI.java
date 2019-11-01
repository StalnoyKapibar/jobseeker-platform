package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Reply;
import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyDaoI extends JpaRepository<Reply, Long> {

    @Query("SELECT distinct r FROM Reply r JOIN r.comment rc where rc=:currentComment")
    List<Reply> getAllRepliesForComment(Comment currentComment);
}
