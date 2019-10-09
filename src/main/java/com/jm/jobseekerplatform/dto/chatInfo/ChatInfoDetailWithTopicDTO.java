package com.jm.jobseekerplatform.dto.chatInfo;

import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
public class ChatInfoDetailWithTopicDTO extends ChatInfoDetailDTO {

    private long topicCreatorProfileId;

    private String topicCreatorName;

    private String topicCreatorType;

    private long topicId;

    private String topicType;

    private String topicTitle;

    public ChatInfoDetailWithTopicDTO(ChatWithTopic chatWithTopic, Long countOfUnreadMessages, ChatMessage lastMessage) {
        super(chatWithTopic, countOfUnreadMessages, lastMessage);
        this.topicTitle = chatWithTopic.getTopic().getHeadline();
        this.topicCreatorProfileId = chatWithTopic.getTopic().getCreatorProfile().getId();
        this.topicCreatorName = chatWithTopic.getTopic().getCreatorProfile().getFullName();
        this.topicCreatorType = chatWithTopic.getTopic().getCreatorProfile().getTypeName();
        this.topicId = chatWithTopic.getTopic().getId();
        this.topicType = chatWithTopic.getTopic().getTypeName();
    }

    public long getTopicCreatorProfileId() {
        return topicCreatorProfileId;
    }

    public void setTopicCreatorProfileId(long topicCreatorProfileId) {
        this.topicCreatorProfileId = topicCreatorProfileId;
    }

    public String getTopicCreatorName() {
        return topicCreatorName;
    }

    public void setTopicCreatorName(String topicCreatorName) {
        this.topicCreatorName = topicCreatorName;
    }

    public String getTopicCreatorType() {
        return topicCreatorType;
    }

    public void setTopicCreatorType(String topicCreatorType) {
        this.topicCreatorType = topicCreatorType;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }
}
