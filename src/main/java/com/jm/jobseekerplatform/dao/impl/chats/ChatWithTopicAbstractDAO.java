package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * <p>Классы <code>ChatWithTopicAbstractDAO</code> (и его наследники)
 * и <code>ChatWithTopicDAO</code> дублируют функционал,
 * однако используют разные реализации.
 *
 * <p> В проектке существует обе реализации для демонстрации разных подходов:
 * - класс <code>ChatWithTopicAbstractDAO</code> требует создавать наследников
 * для каждого типа чатов,
 * - класс <code>ChatWithTopicDAO</code> не требует создания наследников, но
 * требует в качестве параметра методов передавать класс чата или класс темы чата.
 *
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 * @see ChatWithTopicDAO
 */

@Repository
public abstract class ChatWithTopicAbstractDAO<T extends ChatWithTopic> extends AbstractDAO<T> {

    /**
     * Методы <code>getByTopicIdCreatorProfileIdTopicType</code>, <code>getByTopicIdCreatorProfileIdChatType</code>
     * и <code>getByTopicIdCreatorProfileId</code> дублируют функционал, однако используют разгную реализацию.
     * <p>
     * В проекте существуют разные реализации для демонстрации разных подходов.
     *
     * @see {@link com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getByTopicIdCreatorProfileIdTopicType}
     * @see {@link com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getByTopicIdCreatorProfileIdChatType}
     */
    public T getByTopicIdCreatorProfileId(Long topicId, Long creatorProfileId) {

        T chat;

        try {
            chat =
                    entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.creatorProfile.id = :creatorProfileId AND c.topic.id = :topicId", clazz)
                            .setParameter("creatorProfileId", creatorProfileId)
                            .setParameter("topicId", topicId)
                            .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            chat = null;
        }

        return chat;
    }

    /**
     * Возвращает список чатов в которых профиль является участником чата.
     * Участник чата - это профиль, который написал в чат хотя бы одно сообщение
     *
     * @param participantProfileId id профиля
     */

    public List<T> getAllByParticipantProfileId(Long participantProfileId) { //todo (Nick Dolgopolov)

        List<T> chats = entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE :creatorProfileId IN c.chatMessages. ").getResultList();

        return chats;
    }
}
