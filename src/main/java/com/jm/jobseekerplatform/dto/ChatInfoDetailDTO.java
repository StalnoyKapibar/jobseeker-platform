package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;

public class ChatInfoDetailDTO extends ChatInfoDTO {

    private ChatMessage lastMessage;

    private long countOfUnreadMessages;

    public ChatInfoDetailDTO(Chat chat, Long countOfUnreadMessages, ChatMessage lastMessage) {
        super(chat);

        this.countOfUnreadMessages = countOfUnreadMessages;
        this.lastMessage = lastMessage;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getCountOfUnreadMessages() {
        return countOfUnreadMessages;
    }

    public void setCountOfUnreadMessages(long countOfUnreadMessages) {
        this.countOfUnreadMessages = countOfUnreadMessages;
    }
}
