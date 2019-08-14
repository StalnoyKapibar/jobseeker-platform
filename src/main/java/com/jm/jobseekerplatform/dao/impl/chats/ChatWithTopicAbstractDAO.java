package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
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
     * и <code>getChatByTopicIdCreatorProfileId</code> дублируют функционал, однако используют разгную реализацию.
     * <p>
     * В проекте существуют разные реализации для демонстрации разных подходов.
     *
     * @see com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getByTopicIdCreatorProfileIdTopicType
     * @see com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getByTopicIdCreatorProfileIdChatType
     */
    public T getChatByTopicIdCreatorProfileId(Long topicId, Long creatorProfileId) {

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
     * Возвращает список чатов в которых указанный профиль является участником чата.
     * Участник чата - это профиль, который написал в чат хотя бы одно сообщение
     *
     * @param participantProfileId id профиля автора сообщений
     */
    public List<T> getAllChatsByParticipantProfileId(Long participantProfileId) { //todo (Nick Dolgopolov) методы дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.

        List<T> chats = entityManager.createQuery("SELECT DISTINCT c FROM " + clazz.getName() + " c JOIN c.chatMessages m WHERE m.creatorProfile.id = :participantProfileId", clazz).
                setParameter("participantProfileId", participantProfileId)
                .getResultList();

        return chats;
    }


    /**
     * Возвращает список чатов в которых указанный профиль является создателем чата.
     *
     * @param chatCreatorProfileId id профиля создателя чата
     */
    public List<T> getAllChatsByChatCreatorProfileId(Long chatCreatorProfileId) { //todo (Nick Dolgopolov) методы дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.
        List<T> chats = entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.creatorProfile.id = :chatCreatorProfileId", clazz)
                .setParameter("chatCreatorProfileId", chatCreatorProfileId)
                .getResultList();

        return chats;
    }


    /**
     * Возвращает список чатов в которых указанный профиль является создателем темы чата
     * (т.е. создателем сущности, которая является темой чата)
     *
     * @param topicCreatorProfileId id профиля создателя чата
     */
    public List<T> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId) {

        List<T> chats = entityManager.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.topic.creatorProfile.id = :topicCreatorProfileId", clazz)
                .setParameter("topicCreatorProfileId", topicCreatorProfileId)
                .getResultList();

        return chats;
    }

    /**
     * Возвращает список чатов в которых указанный профиль является:
     * - создателем чата,
     * - создателем темы чата,
     * - участником чата.
     * <p>
     * Участник чата - это профиль, который написал в чат хотя бы одно сообщение
     *
     * @param profileId id профиля
     */

    public List<T> getAllChatsByProfileId(Long profileId) { //todo (Nick Dolgopolov) методы дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.
        List<T> chats = entityManager.createQuery("SELECT DISTINCT c FROM " + clazz.getName() + " c " +
                "LEFT JOIN c.chatMessages m " +
                "WHERE c.creatorProfile.id = :profileId " +
                "OR c.topic.creatorProfile.id = :profileId " +
                "OR m.creatorProfile.id = :profileId", clazz)
                .setParameter("profileId", profileId)
                .getResultList();

        return chats;
    }


    public List<T> getAllUnreadChatsByProfileId(Long profileId) { //todo (Nick Dolgopolov) метод дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.

        List<T> listOfUnreadChats = entityManager.createQuery("SELECT DISTINCT c FROM " + clazz.getName() + " c " +
                "JOIN c.chatMessages m1 " +
                "JOIN c.chatMessages m2 " +
                "WHERE :profileId NOT MEMBER OF m1.isReadByProfilesId " +
                "AND (c.creatorProfile.id = :profileId " +
                "OR c.topic.creatorProfile.id = :profileId " +
                "OR m2.creatorProfile.id = :profileId)", clazz)
                .setParameter("profileId", profileId)
                .getResultList();

        return listOfUnreadChats;
    }

    public long getCountOfUnreadChatsByProfileId(Long profileId) { //todo (Nick Dolgopolov) метод дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.

        List<Long> countOfUnreadChats = entityManager.createQuery("SELECT COUNT (DISTINCT c) FROM " + clazz.getName() + " c " +
                "JOIN c.chatMessages m1 " +
                "JOIN c.chatMessages m2 " +
                "WHERE :profileId NOT MEMBER OF m1.isReadByProfilesId " +
                "AND (c.creatorProfile.id = :profileId " +
                "OR c.topic.creatorProfile.id = :profileId " +
                "OR m2.creatorProfile.id = :profileId)", Long.class)
                .setParameter("profileId", profileId)
                .getResultList();

        switch (countOfUnreadChats.size()) {
            case (0):
                return 0;
            case (1):
                return countOfUnreadChats.get(0);
            default:
                throw new RuntimeException("something goes wrong");
        }


// Разные способы обработки getSingleResult:
//        countOfUnreadChats.stream().findFirst().orElse(0L); //примеры обработки getResultList вместо GetSingleResult (NoResultException)
//
//        int size = countOfUnreadChats.size();
//        if (size == 0){
//            return 0;
//        } else if (size == 1){
//            return countOfUnreadChats.get(0);
//        }
//        else {
//            throw new RuntimeException("something goes wrong");
//        }
    }

    public List<ChatInfoDetailWithTopicDTO> getAllChatsInfoDTOByProfileId(Long profileId) { //todo (Nick Dolgopolov) метод дублировать в ChatWithTopicDAO и ChatDAO. Этот метод будет выдавать только чаты, которым от параметризован.

        List<ChatInfoDetailWithTopicDTO> listOfChatInfoDetailWithTopicDTO =
                entityManager.createQuery(
                        "SELECT new com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO(" +
                                "c, count (case " +
                                "when (exists( " +
                                "select m3 " +
                                "from ChatMessage m3 " +
                                "where (m3.id = m.id AND " +
                                "m3 not in " +
                                "( " +
                                " select distinct m4 " +
                                "from ChatMessage m4 " +
                                "join m4.isReadByProfilesId irbpid " +
                                "where (m4.id = m3.id " +
                                " and irbpid = :profileId)" +

                                ")" +
                                "))" +
                                ")" +
                                " then 1 else null end), MAX(m) " +
                                ") " +
                                "FROM " + clazz.getName() + " c " +
                                "JOIN c.chatMessages m " +
                                "WHERE " +
                                "(c in (select distinct c2 " +
                                "from " + clazz.getName() + " c2 " +
                                "JOIN c2.chatMessages m2 " +
                                "where " +
                                "(c2.creatorProfile.id = :profileId " + "OR " +
                                "c2.topic.creatorProfile.id = :profileId " + "OR " +
                                "m2.creatorProfile.id = :profileId) " +
                                " )) " +

                                "GROUP BY c.id", ChatInfoDetailWithTopicDTO.class)
                        .setParameter("profileId", profileId).getResultList();

//        Более строгий запрос. Выбор последнего сообщения по дате, а не по id
//        entityManager.createQuery("SELECT c, COUNT (DISTINCT m1), m2 FROM " + clazz.getName() + " c " +
//                "JOIN c.chatMessages m1 " +
//                "JOIN c.chatMessages m2 " +
//                "WHERE m2.date = (SELECT max(m3.date) FROM " + clazz.getName() + " c1 " +  " JOIN c1.chatMessages m3 WHERE c1.id = c.id)  " +
//                "GROUP BY c.id ")
//                .getResultList()



//запрос в SQL для работы с базой напрямую:
//        select c.id,
//                count(case
//                when (exists(select cm1.id
//                        from chatmessages cm1
//                        where (
//                                cm1.id = ccm.chat_messages_id AND
//                                cm1.id not in (select distinct cmirbp2.chat_message_id
//                                        from chat_message_is_read_by_profiles_id cmirbp2
//                                        where cmirbp2.chat_message_id = cm1.id
//                                        AND cmirbp2.is_read_by_profiles_id = 1)
//                        )
//                ))
//        then 1
//                 else null end)
//        from chats c
//        JOIN chats_chat_messages ccm on c.id = ccm.chat_id
//        where (c.id in (select distinct c.id
//                FROM chats c
//                JOIN chats_chat_messages ccm on c.id = ccm.chat_id
//                JOIN chatmessages cm on ccm.chat_messages_id = cm.id
//                JOIN chat_message_is_read_by_profiles_id cmirbpi on cm.id = cmirbpi.chat_message_id
//                JOIN vacancies v on c.topic_id = v.id
//                WHERE (c.creator_profile_id = 1 OR
//                        v.creator_profile_id = 1 OR
//                        cm.creator_profile_id = 1)))
//        group by c.id;


        return listOfChatInfoDetailWithTopicDTO;
    }
}