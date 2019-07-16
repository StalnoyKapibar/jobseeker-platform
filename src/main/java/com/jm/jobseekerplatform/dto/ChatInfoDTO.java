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

    private long participantProfileId;

    private String participantName;

    private String participantType;

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
        chatInfoDTO.creatorName = chatWithTopic.getCreator().GetNameForUi();
        chatInfoDTO.creatorType = chatWithTopic.getCreator().getClass().getName();

        chatInfoDTO.participantProfileId = chatWithTopic.getTopic().getCreator().getId();
        chatInfoDTO.participantName = chatWithTopic.getTopic().getCreator().GetNameForUi();
        chatInfoDTO.participantType = chatWithTopic.getTopic().getCreator().getClass().getName();

        chatInfoDTO.topicId = chatWithTopic.getTopic().getId();
        chatInfoDTO.topicType = chatWithTopic.getTopic().getClass().getName();

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

    public long getParticipantProfileId() {
        return participantProfileId;
    }

    public void setParticipantProfileId(long participantProfileId) {
        this.participantProfileId = participantProfileId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantType() {
        return participantType;
    }

    public void setParticipantType(String participantType) {
        this.participantType = participantType;
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
