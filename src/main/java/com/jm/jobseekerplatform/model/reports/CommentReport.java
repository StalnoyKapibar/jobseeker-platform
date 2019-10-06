package com.jm.jobseekerplatform.model.reports;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Comment")
public class CommentReport extends Report implements Serializable {

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "reports_comment", joinColumns = {@JoinColumn(name = "report_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "id")})
    private Comment comment;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "reports_author", joinColumns = {@JoinColumn(name = "report_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")})
    private Profile author;

    public CommentReport() {
    }

    public CommentReport(String dateTime, String description, Comment comment, Profile author) {
        super(dateTime, description);
        this.comment = comment;
        this.author = author;
    }

    public CommentReport(String dateTime, String description, Comment comment) {
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
