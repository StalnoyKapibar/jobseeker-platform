package com.jm.jobseekerplatform.dto;

public class MessageReadDataDTO {

    private Long chatId;
    private Long messageId;
    private Long readerProfileId;

    public Long getMessageId() {
        return messageId;
    }

    public Long getReaderProfileId() {
        return readerProfileId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
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
