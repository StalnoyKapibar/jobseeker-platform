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
    private Date mTimestamp;

    public NewChatMessage() {
    }

    public NewChatMessage(Long chatId, Profile mOwner, String message, Date mTimestamp) {
        this.chatId = chatId;
        this.mOwner = mOwner;
        this.message = message;
        this.mTimestamp = mTimestamp;
    }
}
