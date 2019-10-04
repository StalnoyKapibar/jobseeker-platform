package com.jm.jobseekerplatform.model.reports;

import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reports")
public class Report implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReportType type;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile profile;

    public Report() {
    }

    public Report(ReportType type) {
        this.type = type;
    }

    public Report(ReportType type, Comment comment, Profile profile) {
        this.type = type;
        this.comment = comment;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Profile getAuthor() {
        return profile;
    }

    public void setAuthor(Profile profile) {
        this.profile = profile;
    }
}
