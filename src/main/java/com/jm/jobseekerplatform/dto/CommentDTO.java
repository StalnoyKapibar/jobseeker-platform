package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.comments.Comment;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class CommentDTO extends PageImpl<Comment> {
    private List<Comment> comments;
    private int currentPage;
    private int totalPages;

    public CommentDTO(List<Comment> comments, int currentPage, int totalPages) {
        super(comments);
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
