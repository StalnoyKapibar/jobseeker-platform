package com.jm.jobseekerplatform.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageDTO {

    private Long id;

    private Long creatorProfileId;

    private List<String> members;

    private String text;

    private Date date;

    public MessageDTO(Long creatorProfileId, List<String> members, String text, Date date) {
        this.creatorProfileId = creatorProfileId;
        this.members = members;
        this.text = text;
        this.date = date;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembersId(List<String> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creatorProfileId, that.creatorProfileId) &&
                Objects.equals(members, that.members) &&
                Objects.equals(text, that.text) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorProfileId, members, text, date);
    }
}
