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
public class NewChatMember {
    @Id
    @GeneratedValue
    private Long id;

    private Profile participant;
    // @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime timestamp;

    public Profile getParticipant() {
        return participant;
    }

    public void setParticipant(Profile participant) {
        this.participant = participant;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public NewChatMember(Profile participant, Long chatId) {
        this.participant = participant;
        this.chatId = chatId;
        this.timestamp = LocalDateTime.now();
    }

    public NewChatMember() {
    }

}
