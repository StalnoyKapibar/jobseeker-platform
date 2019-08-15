package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
public class ChatInfoDTO {


    private long id;

    private long creatorProfileId;

    private String creatorName;

    private String creatorType;

    private long topicCreatorProfileId;

    private String topicCreatorName;

    private String topicCreatorType;

    private long topicId;

    private String topicType;

    private String topicTitle;

    private ChatMessage lastMessage;

    public static ChatInfoDTO fromChatWithTopic(ChatWithTopicVacancy chatWithTopic) {
        ChatInfoDTO chatInfoDTO = new ChatInfoDTO();

        chatInfoDTO = fromChatWithTopicCommon(chatInfoDTO, chatWithTopic);

        chatInfoDTO.topicTitle = chatWithTopic.getTopic().getHeadline();

        return chatInfoDTO;
    }

    private static ChatInfoDTO fromChatWithTopicCommon(ChatInfoDTO chatInfoDTO, ChatWithTopic chatWithTopic) {

        chatInfoDTO.id = chatWithTopic.getId();

        chatInfoDTO.creatorProfileId = chatWithTopic.getCreator().getId();
        chatInfoDTO.creatorName = chatWithTopic.getCreator().getFullName();
        chatInfoDTO.creatorType = chatWithTopic.getCreator().getTypeName();

        chatInfoDTO.topicCreatorProfileId = chatWithTopic.getTopic().getCreatorProfile().getId();
        chatInfoDTO.topicCreatorName = chatWithTopic.getTopic().getCreatorProfile().getFullName();
        chatInfoDTO.topicCreatorType = chatWithTopic.getTopic().getCreatorProfile().getTypeName();

        chatInfoDTO.topicId = chatWithTopic.getTopic().getId();
        chatInfoDTO.topicType = chatWithTopic.getTopic().getTypeName();

        return chatInfoDTO;
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

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
