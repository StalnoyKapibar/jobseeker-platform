/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class NewChatMessage {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column
    private Profile mOwner;
    @Column
    private String message;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime mTimestamp;

    public NewChatMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Profile getmOwner() {
        return mOwner;
    }

    public void setmOwner(Profile mOwner) {
        this.mOwner = mOwner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(LocalDateTime mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public NewChatMessage(Long chatId, Profile mOwner, String message) {
        this.chatId = chatId;
        this.mOwner = mOwner;
        this.message = message;
        this.mTimestamp = LocalDateTime.now();
    }
}
