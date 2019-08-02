package com.jm.jobseekerplatform.dto;

import java.util.Objects;

public class MessageDTO {

    private Long id;

    private Long creatorProfileId;

    private String text;

    private boolean isRead;

    public MessageDTO(Long creatorProfileId, String text, boolean isRead) {
        this.creatorProfileId = creatorProfileId;
        this.text = text;
        this.isRead = isRead;
    }

    public Long getCreatorProfileId() {
        return creatorProfileId;
    }

    public void setCreatorProfileId(Long creatorProfileId) {
        this.creatorProfileId = creatorProfileId;
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
                creatorProfileId.equals(that.creatorProfileId) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorProfileId, text, isRead);
    }
}
