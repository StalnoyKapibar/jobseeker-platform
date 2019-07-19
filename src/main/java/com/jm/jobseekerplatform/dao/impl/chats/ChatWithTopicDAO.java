package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.CreatedByProfile;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Repository
public class ChatWithTopicDAO extends AbstractDAO<ChatWithTopic> {

    public <T extends CreatedByProfile> ChatWithTopic<T> getByTopicIdCreatorProfileIdTopicType(Long topicId, Long creatorProfileId, Class<T> topicClass) {

        ChatWithTopic<T> chat;

        try {
            chat =
                    entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c JOIN treat (c.topic as " + topicClass.getName() + ") WHERE c.creatorProfile.id = :creatorProfileId AND c.topic.id = :topicId", clazz)
                            .setParameter("creatorProfileId", creatorProfileId)
                            .setParameter("topicId", topicId)
                            .getSingleResult();

        } catch (NoResultException e) {
            e.printStackTrace();
            chat = null;
        }

        return chat;
    }


    public <T extends ChatWithTopic> T getByTopicIdCreatorProfileIdChatType(Long topicId, Long creatorProfileId, Class<T> ChatClass) {

        T chat;

        try {
            chat =
                    entityManager.createQuery("SELECT c FROM " + ChatClass.getName() + " c WHERE c.creatorProfile.id = :creatorProfileId AND c.topic.id = :topicId", ChatClass)
                            .setParameter("creatorProfileId", creatorProfileId)
                            .setParameter("topicId", topicId)
                            .getSingleResult();

        } catch (NoResultException e) {
            e.printStackTrace();
            chat = null;
        }

        return chat;
    }
}
