package com.jm.jobseekerplatform.dto.chatInfo;

import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;

import java.util.Date;

public class ChatInfoDetailDTO extends ChatInfoDTO {

    private long countOfUnreadMessages;

    private String lastMessageText;
    private Date lastMessageDate;
    private long lastMessageProfileId;
    private String lastMessageCreatorName;
    private String lastMessageCreatorType;

    public ChatInfoDetailDTO(Chat chat, Long countOfUnreadMessages, ChatMessage lastMessage) {
        super(chat);
        this.countOfUnreadMessages = countOfUnreadMessages;

        if (lastMessage != null) {
            this.lastMessageText = lastMessage.getText();
            this.lastMessageDate = lastMessage.getDate();
            this.lastMessageProfileId = lastMessage.getCreatorProfile().getId();
            this.lastMessageCreatorName = lastMessage.getCreatorProfile().getFullName();
            this.lastMessageCreatorType = lastMessage.getCreatorProfile().getTypeName();
        } else {
            this.lastMessageText = "no message";
            this.lastMessageDate = new Date(0);
            this.lastMessageProfileId = 0;
            this.lastMessageCreatorName = "";
            this.lastMessageCreatorType = "";
        }
    }

    public long getCountOfUnreadMessages() {
        return countOfUnreadMessages;
    }

    public void setCountOfUnreadMessages(long countOfUnreadMessages) {
        this.countOfUnreadMessages = countOfUnreadMessages;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public long getLastMessageProfileId() {
        return lastMessageProfileId;
    }

    public void setLastMessageProfileId(long lastMessageProfileId) {
        this.lastMessageProfileId = lastMessageProfileId;
    }

    public String getLastMessageCreatorName() {
        return lastMessageCreatorName;
    }

    public void setLastMessageCreatorName(String lastMessageCreatorName) {
        this.lastMessageCreatorName = lastMessageCreatorName;
    }

    public String getLastMessageCreatorType() {
        return lastMessageCreatorType;
    }

    public void setLastMessageCreatorType(String lastMessageCreatorType) {
        this.lastMessageCreatorType = lastMessageCreatorType;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }
}
