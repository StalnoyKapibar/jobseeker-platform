package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chatmessages")
public class ChatMessage implements Serializable, Comparable<ChatMessage> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "author")
    private String author;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "adminFrom")
    private String adminFrom;

    @Column(name = "adminTo")
    private String adminTo;

    public ChatMessage(){ }

    public ChatMessage(String text, String author, Date createDate, String adminFrom, String adminTo) {
        this.text = text;
        this.author = author;
        this.createDate = createDate;
        this.adminFrom = adminFrom;
        this.adminTo = adminTo;
    }

    public Long getId() { return id; }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAdminFrom() { return adminFrom; }

    public void setAdminFrom(String adminFrom) { this.adminFrom = adminFrom; }

    public String getAdminTo() { return adminTo; }

    public void setAdminTo(String adminTo) { this.adminTo = adminTo; }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ",\"text\":\"" + text + '\"' +
                ",\"author\":\"" + author + '\"' +
                ",\"createDate\":\"" + createDate + "\"" +
                '}';
    }

    @Override
    public int compareTo(ChatMessage o) {
        return Long.compare(o.getCreateDate().getTime(), this.getCreateDate().getTime());
    }
}
