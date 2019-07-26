package com.jm.jobseekerplatform.dao.impl.chats;

        import com.jm.jobseekerplatform.dao.AbstractDAO;
        import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
        import org.springframework.stereotype.Repository;

        import javax.persistence.NoResultException;
        import javax.persistence.Query;
        import java.util.List;

/**
 * <p> Классы <code>ChatWithTopicAbstractDAO</code> (и его наследники)
 * и <code>ChatWithTopicDAO</code> дублируют функционал,
 * однако используют разные реализации.
 *
 * В проектке существует оба класса для демонстрации разных подходов:
 * - класс <code>ChatWithTopicAbstractDAO</code> требует создавать наследников
 * для каждого типа чатов,
 * - класс <code>ChatWithTopicDAO</code> не требует создания наследников, но
 * требует в качестве параметра методов передавать класс чата или класс темы чата.
 *
 * @see ChatWithTopicDAO
 *
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Repository
public abstract class ChatWithTopicAbstractDAO<T extends ChatWithTopic> extends AbstractDAO<T> {

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

    public List<T> getAllByParticipantProfileId(Long participantProfileId){ //todo (Nick Dolgopolov)

        List<T> chats = entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.creatorProfile.id = :creatorProfileId").getResultList();

        return chats;
    }
}
