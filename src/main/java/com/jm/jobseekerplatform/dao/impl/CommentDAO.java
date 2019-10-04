package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

@Repository("commentDAO")
public class CommentDAO extends AbstractDAO<Comment> {

	public List<Comment> getAllCommentsForNews(News news){
		Query query = entityManager.createQuery("SELECT distinct c FROM Comment c JOIN c.news cn where cn=:currentNews", Comment.class);
		query.setParameter("currentNews", news);
		List<Comment> newsComments = query.getResultList();
		return newsComments;
	}
}