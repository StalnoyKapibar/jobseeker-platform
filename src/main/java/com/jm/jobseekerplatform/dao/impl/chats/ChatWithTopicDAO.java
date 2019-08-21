package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfile;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Repository
public class ChatWithTopicDAO extends AbstractDAO<ChatWithTopic> {

    // todo сделать дженерик методы
//    public List<T> getAll() {
//        return entityManager.createQuery("SELECT e FROM " + clazz.getName() + " e", clazz).getResultList();
//    }
//
//    public List<T> getAllWithLimit(int limit) {
//        return entityManager.createQuery("SELECT e FROM " + clazz.getName() + " e", clazz).setMaxResults(limit).getResultList();
//    }

    public <T extends ChatWithTopic> List<ChatInfoWithTopicDTO> getAllChatsInfoDTO(Class<T> ChatClass) {
        List<ChatInfoWithTopicDTO> listOfChatInfoWithTopicDTO =
                entityManager.createQuery(
                        "SELECT new com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO(c) " +
                                "FROM " + ChatClass.getName() + " c " +
                                "ORDER BY c.id", ChatInfoWithTopicDTO.class)
                        .getResultList();
        return listOfChatInfoWithTopicDTO;
    }

    public List<ChatInfoWithTopicDTO> getAllChatsInfoDTO() {
        List<ChatInfoWithTopicDTO> listOfChatInfoWithTopicDTO = getAllChatsInfoDTO(clazz);
        return listOfChatInfoWithTopicDTO;
    }


    public <T extends ChatWithTopic> List<ChatInfoDetailWithTopicDTO> getAllChatsInfoDTOByProfileId(Long profileId, Class<T> ChatClass) {

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
                                "FROM " + ChatClass.getName() + " c " +
                                "JOIN c.chatMessages m " +
                                "WHERE " +
                                "(c in (select distinct c2 " +
                                "from " + ChatClass.getName() + " c2 " +
                                "JOIN c2.chatMessages m2 " +
                                "where " +
                                "(c2.creatorProfile.id = :profileId " + "OR " +
                                "c2.topic.creatorProfile.id = :profileId " + "OR " +
                                "m2.creatorProfile.id = :profileId) " +
                                " )) " +

                                "GROUP BY c.id " +
                                "ORDER BY c.id", ChatInfoDetailWithTopicDTO.class)
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

    public List<ChatInfoDetailWithTopicDTO> getAllChatsInfoDTOByProfileId(Long profileId) {

        List<ChatInfoDetailWithTopicDTO> listOfChatInfoDetailWithTopicDTO = getAllChatsInfoDTOByProfileId(profileId, clazz);
        return listOfChatInfoDetailWithTopicDTO;
    }

    /**
     * Возвращает список чатов в которых указанный профиль является участником чата.
     * Участник чата - это профиль, который написал в чат хотя бы одно сообщение
     *
     * @param participantProfileId id профиля автора сообщений
     */
    public <T extends ChatWithTopic> List<T> getAllChatsByParticipantProfileId(Long participantProfileId, Class<T> ChatClass) {

        List<T> chats = entityManager.createQuery("SELECT DISTINCT c FROM " + ChatClass.getName() + " c JOIN c.chatMessages m WHERE m.creatorProfile.id = :participantProfileId", ChatClass).
                setParameter("participantProfileId", participantProfileId)
                .getResultList();

        return chats;
    }

    public  List<ChatWithTopic> getAllChatsByParticipantProfileId(Long participantProfileId) {

        List<ChatWithTopic> chats = getAllChatsByParticipantProfileId(participantProfileId, clazz);

        return chats;
    }

    /**
     * Возвращает список чатов в которых указанный профиль является создателем чата.
     *
     * @param chatCreatorProfileId id профиля создателя чата
     */
    public <T extends ChatWithTopic> List<T> getAllChatsByChatCreatorProfileId(Long chatCreatorProfileId, Class<T> chatClass) {
        List<T> chats = entityManager.createQuery("SELECT c FROM " + chatClass.getName() + " c WHERE c.creatorProfile.id = :chatCreatorProfileId", chatClass)
                .setParameter("chatCreatorProfileId", chatCreatorProfileId)
                .getResultList();

        return chats;
    }

    public List<ChatWithTopic> getAllChatsByChatCreatorProfileId(Long chatCreatorProfileId) {
        List<ChatWithTopic> chats = getAllChatsByChatCreatorProfileId(chatCreatorProfileId, ChatWithTopic.class);
        return chats;
    }

    /**
     * Возвращает список чатов в которых указанный профиль является создателем темы чата
     * (т.е. создателем сущности, которая является темой чата)
     *
     * @param topicCreatorProfileId id профиля создателя чата
     */
    public <T extends ChatWithTopic>  List<T> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId, Class<T> chatClass) {

        List<T> chats = entityManager.createQuery("SELECT c FROM " + chatClass.getName() + " c WHERE c.topic.creatorProfile.id = :topicCreatorProfileId", chatClass)
                .setParameter("topicCreatorProfileId", topicCreatorProfileId)
                .getResultList();

        return chats;
    }

    public List<ChatWithTopic> getAllChatsByTopicCreatorProfileId(Long topicCreatorProfileId) {

        List<ChatWithTopic> chats = getAllChatsByTopicCreatorProfileId(topicCreatorProfileId, clazz);

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

    public <T extends ChatWithTopic> List<T> getAllChatsByProfileId(Long profileId, Class<T> chatClass) {
        List<T> chats = entityManager.createQuery("SELECT DISTINCT c FROM " + chatClass.getName() + " c " +
                "LEFT JOIN c.chatMessages m " +
                "WHERE c.creatorProfile.id = :profileId " +
                "OR c.topic.creatorProfile.id = :profileId " +
                "OR m.creatorProfile.id = :profileId", chatClass)
                .setParameter("profileId", profileId)
                .getResultList();

        return chats;
    }

    public List<ChatWithTopic> getAllChatsByProfileId(Long profileId) {
        List<ChatWithTopic> chats = getAllChatsByProfileId(profileId, clazz);

        return chats;
    }


    public <T extends ChatWithTopic> List<T> getAllUnreadChatsByProfileId(Long profileId, Class<T> chatClass) {

        List<T> listOfUnreadChats = entityManager.createQuery("SELECT DISTINCT c FROM " + chatClass.getName() + " c " +
                "JOIN c.chatMessages m1 " +
                "JOIN c.chatMessages m2 " +
                "WHERE :profileId NOT MEMBER OF m1.isReadByProfilesId " +
                "AND (c.creatorProfile.id = :profileId " +
                "OR c.topic.creatorProfile.id = :profileId " +
                "OR m2.creatorProfile.id = :profileId)", chatClass)
                .setParameter("profileId", profileId)
                .getResultList();

        return listOfUnreadChats;
    }

    public List<ChatWithTopic> getAllUnreadChatsByProfileId(Long profileId) {

        List<ChatWithTopic> listOfUnreadChats = getAllUnreadChatsByProfileId(profileId, clazz);

        return listOfUnreadChats;
    }

    public <T extends ChatWithTopic> long getCountOfUnreadChatsByProfileId(Long profileId, Class<T> ChatClass) {

        List<Long> countOfUnreadChats = entityManager.createQuery("SELECT COUNT (DISTINCT c) FROM " + ChatClass.getName() + " c " +
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

    public long getCountOfUnreadChatsByProfileId(Long profileId) {

        return getCountOfUnreadChatsByProfileId(profileId, clazz);
    }

    /**
     * Методы <code>getByTopicIdCreatorProfileIdTopicType</code> и <code>getByTopicIdCreatorProfileIdChatType</code>
     * дублируют функционал, однако используют разную реализацию (для демонстрации)
     *
     * @see {@link com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getChatByTopicIdCreatorProfileIdChatType}
     */
    public <T extends CreatedByProfile> ChatWithTopic<T> getChatByTopicIdCreatorProfileIdTopicType(Long topicId, Long creatorProfileId, Class<T> topicClass) {

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

    /**
     * Методы <code>getByTopicIdCreatorProfileIdTopicType</code>, <code>getByTopicIdCreatorProfileIdChatType</code>
     * и <code>getChatByTopicIdCreatorProfileId</code> дублируют функционал, однако используют разгную реализацию.
     *
     * @see {@link com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicDAO#getChatByTopicIdCreatorProfileIdTopicType}
     */
    public <T extends ChatWithTopic> T getChatByTopicIdCreatorProfileIdChatType(Long topicId, Long creatorProfileId, Class<T> ChatClass) {

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
