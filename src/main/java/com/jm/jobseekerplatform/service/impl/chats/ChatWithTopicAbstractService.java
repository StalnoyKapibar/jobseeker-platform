package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicAbstractDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class ChatWithTopicAbstractService<T extends ChatWithTopic> extends AbstractService<T> {

    @Autowired
    private ChatWithTopicAbstractDAO<T> chatWithTopicDAO;

    public T getByTopicIdAndCreatorProfileId(Long topicId, Long profileId) {
        return chatWithTopicDAO.getChatByTopicIdCreatorProfileId(topicId, profileId);
    }

    public List<T> getAllByParticipantProfileId(Long participantProfileId) {
        return chatWithTopicDAO.getAllChatsByParticipantProfileId(participantProfileId);
    }

    public List<T> getAllByChatCreatorProfileId(Long chatCreatorProfileId) {
        return chatWithTopicDAO.getAllChatsByChatCreatorProfileId(chatCreatorProfileId);
    }

    public List<T> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId) {
        return chatWithTopicDAO.getAllChatsByTopicCreatorProfileId(topicCreatorProfileId);
    }
}