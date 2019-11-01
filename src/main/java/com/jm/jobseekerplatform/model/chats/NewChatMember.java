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
import java.util.Date;

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
    private Date timestamp;

    public Profile getParticipant() {
        return participant;
    }

    public void setParticipant(Profile participant) {
        this.participant = participant;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public NewChatMember(Profile participant, Long chatId, Date timestamp) {
        this.participant = participant;
        this.chatId = chatId;
        this.timestamp = timestamp;
    }

    public NewChatMember() {
    }

}
