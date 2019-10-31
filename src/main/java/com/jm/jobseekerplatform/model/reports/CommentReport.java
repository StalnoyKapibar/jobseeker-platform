package com.jm.jobseekerplatform.model.reports;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Comment")
public class CommentReport extends Report implements Serializable {

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile author;

    public CommentReport() {
    }

    public CommentReport(LocalDateTime dateTime, String description, Comment comment, Profile author) {
        super(dateTime, description);
        this.comment = comment;
        this.author = author;
    }

    public CommentReport(LocalDateTime dateTime, String description, Comment comment) {
        super(dateTime, description);
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }
}
