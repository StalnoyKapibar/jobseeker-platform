package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ChatWithTopicService<T extends ChatWithTopic> extends AbstractService<T> {

    @Autowired
    private ChatWithTopicDAO<T> chatWithTopicDAO;

    public T getByTopicIdAndCreatorProfileId(Long topicId, Long profileId) {
        return chatWithTopicDAO.getByTopicIdAndCreatorProfileId(topicId, profileId);
    }
}