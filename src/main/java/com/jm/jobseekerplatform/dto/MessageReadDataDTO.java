package com.jm.jobseekerplatform.dto;

public class MessageReadDataDTO {

    private Long chatId;
    private Long lastReadMessageId;
    private Long readerProfileId;

    public Long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public Long getReaderProfileId() {
        return readerProfileId;
    }

    public void setLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public void setReaderProfileId(Long readerProfileId) {
        this.readerProfileId = readerProfileId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
