package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository("chatMessageDAO")
public class ChatMessageDAO extends AbstractDAO<ChatMessage> {
    public void setMessageReadByProfileId(Long readerProfileId, Long messageId) {
        entityManager
                .createNativeQuery("INSERT INTO chat_message_is_read_by_profiles_id (chat_message_id, is_read_by_profiles_id) " +
                        "SELECT cmirbpi.chat_message_id, :readerProfileId " +
                        "FROM chat_message_is_read_by_profiles_id cmirbpi " +
                        "WHERE cmirbpi.chat_message_id = :messageId " +
                        "  AND not exists( " +
                        "        SELECT * " +
                        "        FROM chat_message_is_read_by_profiles_id cmirbpi2 " +
                        "        WHERE cmirbpi2.is_read_by_profiles_id = :readerProfileId " +
                        "          AND cmirbpi2.chat_message_id = cmirbpi.chat_message_id)")
                .setParameter("readerProfileId", readerProfileId)
                .setParameter("messageId", messageId)
                .executeUpdate();
    }
}
