package com.jm.jobseekerplatform.service.impl.comments;

import com.jm.jobseekerplatform.dao.impl.CommentDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("commentService")
@Transactional
public class CommentService extends AbstractService<Comment> {
	@Autowired
	private CommentDAO commentDAO;

	public List<Comment> getAllCommentsForNews(News news) {
		return commentDAO.getAllCommentsForNews(news);
	}
}