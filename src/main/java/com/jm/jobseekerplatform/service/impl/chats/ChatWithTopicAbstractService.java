package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicAbstractDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ChatWithTopicAbstractService<T extends ChatWithTopic> extends AbstractService<T> {

    @Autowired
    private ChatWithTopicAbstractDAO<T> chatWithTopicDAO;

    public T getByTopicIdAndCreatorProfileId(Long topicId, Long profileId) {
        return chatWithTopicDAO.getByTopicIdCreatorProfileId(topicId, profileId);
    }
}