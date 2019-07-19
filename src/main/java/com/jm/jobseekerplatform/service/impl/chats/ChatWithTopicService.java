package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO;
import com.jm.jobseekerplatform.model.CreatedByProfile;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatWithTopicService extends AbstractService<ChatWithTopic> {

    @Autowired
    private ChatWithTopicDAO chatWithTopicDAO;

    public <T extends CreatedByProfile> ChatWithTopic<T> getByTopicIdCreatorProfileIdTopicType(Long topicId, Long creatorProfileId, Class<T> topicClass) {
        return chatWithTopicDAO.getByTopicIdCreatorProfileIdTopicType(topicId, creatorProfileId, topicClass);
    }

    public <T extends ChatWithTopic> T getByTopicIdCreatorProfileIdChatType(Long topicId, Long creatorProfileId, Class<T> ChatClass) {
        return chatWithTopicDAO.getByTopicIdCreatorProfileIdChatType(topicId, creatorProfileId, ChatClass);
    }
}
