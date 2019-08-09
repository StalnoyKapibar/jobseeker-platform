package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.chats.Chat;

public class ChatInfoDTO {

    private long id;

    private long creatorProfileId;

    private String creatorName;

    private String creatorType;

    public ChatInfoDTO(Chat chat) {
        this.id = chat.getId();

        this.creatorProfileId = chat.getCreator().getId();
        this.creatorName = chat.getCreator().getFullName();
        this.creatorType = chat.getCreator().getTypeName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatorProfileId() {
        return creatorProfileId;
    }

    public void setCreatorProfileId(long creatorProfileId) {
        this.creatorProfileId = creatorProfileId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }
}
