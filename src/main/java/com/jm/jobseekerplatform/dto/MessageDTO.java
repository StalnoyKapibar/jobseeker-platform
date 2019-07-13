package com.jm.jobseekerplatform.dto;

import java.util.Objects;

public class MessageDTO {

    private Long id;

    private Long authorId;

    private String text;

    private boolean isRead;

    public MessageDTO(Long authorId, String text, boolean isRead) {
        this.authorId = authorId;
        this.text = text;
        this.isRead = isRead;
    }

    public Long getAuthor() {
        return authorId;
    }

    public void setAuthor(Long authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return isRead == that.isRead &&
                id.equals(that.id) &&
                authorId.equals(that.authorId) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId, text, isRead);
    }
}
