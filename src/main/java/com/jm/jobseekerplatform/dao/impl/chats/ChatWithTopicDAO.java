package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Repository
public abstract class ChatWithTopicDAO<T extends ChatWithTopic> extends AbstractDAO<T> {

    public T getByTopicIdAndCreatorProfileId(Long topicId, Long creatorProfileId) {

        T chat;

        try {
            chat =
                    entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.creatorProfile.id = :creatorProfileId AND c.topic.id = :topicId", clazz)
                            .setParameter("creatorProfileId", creatorProfileId)
                            .setParameter("topicId", topicId)
                            .getSingleResult();
        }
        catch (NoResultException e){
            e.printStackTrace();
            chat = null;
        }

        return chat;
    }
}
