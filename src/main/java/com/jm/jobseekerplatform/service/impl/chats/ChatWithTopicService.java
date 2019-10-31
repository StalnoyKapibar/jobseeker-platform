package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.interfaces.chats.ChatWithTopicDao;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatWithTopicService extends AbstractService<ChatWithTopic> {

    @Autowired
    private ChatWithTopicDao chatWithTopicDAO;

    public <T extends ChatWithTopic> List<T> getAll(Class<T> chatClass) {
        return chatWithTopicDAO.getAll(chatClass);
    }

    public <T extends ChatWithTopic> List<T> getAllWithLimit(int limit, Class<T> chatClass) {
        return chatWithTopicDAO.getAllWithLimit(limit, chatClass);
    }

    public <T extends ChatWithTopic> List<ChatInfoWithTopicDTO> getAllChatsInfoDTO(Class<T> chatClass) {
        return chatWithTopicDAO.getAllChatsInfoDTO(chatClass);
    }

    public List<ChatInfoWithTopicDTO> getAllChatsInfoDTO() {
        return chatWithTopicDAO.getAllChatsInfoDTO();
    }

    public <T extends ChatWithTopic> List<ChatInfoDetailWithTopicDTO> getAllChatsInfoDTOByProfileId(Long profileId, Class<T> chatClass) {
        return chatWithTopicDAO.getAllChatsInfoDTOByProfileId(profileId, chatClass);
    }

    public List<ChatInfoDetailWithTopicDTO> getAllChatsInfoDTOByProfileId(Long profileId) {
        return chatWithTopicDAO.getAllChatsInfoDTOByProfileId(profileId);
    }

    public <T extends ChatWithTopic> List<T> getAllByChatCreatorProfileId(Long chatCreatorProfileId, Class<T> chatClass) {
        return chatWithTopicDAO.getAllChatsByChatCreatorProfileId(chatCreatorProfileId, chatClass);
    }

    public List<ChatWithTopic> getAllByChatCreatorProfileId(Long chatCreatorProfileId) {
        return chatWithTopicDAO.getAllChatsByChatCreatorProfileId(chatCreatorProfileId);
    }

    public <T extends ChatWithTopic> List<T> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId, Class<T> chatClass) {
        return chatWithTopicDAO.getAllChatsByTopicCreatorProfileId(topicCreatorProfileId, chatClass);
    }

    public List<ChatWithTopic> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId) {
        return chatWithTopicDAO.getAllChatsByTopicCreatorProfileId(topicCreatorProfileId);
    }

    public <T extends ChatWithTopic> List<T> getAllUnreadChatsByProfileId(Long profileId, Class<T> chatClass) {
        return chatWithTopicDAO.getAllUnreadChatsByProfileId(profileId, chatClass);
    }

    public List<ChatWithTopic> getAllUnreadChatsByProfileId(Long profileId) {
        return chatWithTopicDAO.getAllUnreadChatsByProfileId(profileId);
    }

    public <T extends ChatWithTopic> long getCountOfUnreadChatsByProfileId(Long profileId, Class<T> chatClass) {
        return chatWithTopicDAO.getCountOfUnreadChatsByProfileId(profileId, chatClass);
    }

    public long getCountOfUnreadChatsByProfileId(Long profileId) {
        return chatWithTopicDAO.getCountOfUnreadChatsByProfileId(profileId);
    }

    public <T extends CreatedByProfile> ChatWithTopic<T> getChatByTopicIdCreatorProfileIdTopicType(Long topicId, Long creatorProfileId, Class<T> topicClass) {
        return chatWithTopicDAO.getChatByTopicIdCreatorProfileIdTopicType(topicId, creatorProfileId, topicClass);
    }

    public <T extends ChatWithTopic> T getChatByTopicIdCreatorProfileIdChatType(Long topicId, Long creatorProfileId, Class<T> ChatClass) {
        return chatWithTopicDAO.getChatByTopicIdCreatorProfileIdChatType(topicId, creatorProfileId, ChatClass);
    }

    public <T extends ChatWithTopic> List<T> getAllChatsByMemberProfileId(Long profileId, Class<T> chatClass) {
        return chatWithTopicDAO.getAllChatsByMemberProfileId(profileId, chatClass);
    }

    public List<ChatWithTopic> getAllChatsByMemberProfileId(Long profileId) {
        return chatWithTopicDAO.getAllChatsByMemberProfileId(profileId);
    }

    public ChatWithTopic getChatByMessageId(Long messageId) {
        return chatWithTopicDAO.getChatByMessageId(messageId);
    }

}
