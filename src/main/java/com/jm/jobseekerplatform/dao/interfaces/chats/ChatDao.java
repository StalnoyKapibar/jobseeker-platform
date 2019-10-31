package com.jm.jobseekerplatform.dao.interfaces.chats;

import com.jm.jobseekerplatform.dao.AbstractDao;
import com.jm.jobseekerplatform.model.chats.Chat;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository("chatDAO")
public class ChatDao extends AbstractDao<Chat> {

    public void setChatReadByProfileId(Long chatId, Long readerProfileId, Long lastReadMessageId) {
        entityManager
                .createNativeQuery("INSERT INTO chat_message_is_read_by_profiles_id " +
                                "(chat_message_id, is_read_by_profiles_id) " +
                        "SELECT cmirbpi.chat_message_id, :readerProfileId " +
                        "FROM chat_message_is_read_by_profiles_id cmirbpi " +
                        "         JOIN chats_chat_messages ccm ON cmirbpi.chat_message_id = ccm.chat_messages_id " +
                        "         JOIN chats c ON ccm.chat_id = c.id " +
                        "WHERE c.id = :chatId " +
                        "  AND cmirbpi.chat_message_id <= :lastReadMessageId " +
                        "  AND not exists( " +
                        "        SELECT * " +
                        "        FROM chat_message_is_read_by_profiles_id cmirbpi2 " +
                        "        WHERE cmirbpi2.is_read_by_profiles_id = :readerProfileId " +
                        "          AND cmirbpi2.chat_message_id = cmirbpi.chat_message_id)")
                .setParameter("readerProfileId", readerProfileId)
                .setParameter("chatId", chatId)
                .setParameter("lastReadMessageId", lastReadMessageId)
                .executeUpdate();
    }

    public List<BigInteger> getChatMembersIds(Long chatId) {
        return entityManager.createNativeQuery("SELECT chat_members_id FROM chats_chat_members chatmembers " +
                "WHERE chatmembers.chat_id = :chatId").setParameter("chatId", chatId).getResultList();
    }

	public List<BigInteger> getProfileIDByChatDAO(Long chatId) {
		return entityManager.createNativeQuery("SELECT is_read_by_profiles_id " +
				"FROM chat_message_is_read_by_profiles_id " +
				"where chat_message_id = (SELECT max(chat_messages_id) " +
				"FROM chats_chat_messages where chat_id = :chatId)")
				.setParameter("chatId", chatId)
				.getResultList();
	}

	public List<Object> getAllIdLastMessagesByUserIdDAO(Long userId, Long profileId) {
		return entityManager.createNativeQuery("SELECT is_read_by_profiles_id " +
				"FROM chat_message_is_read_by_profiles_id " +
				"where chat_message_id IN (SELECT max(chat_messages_id) " +
				"FROM chats_chat_messages where chat_id IN (SELECT chat_id " +
				"FROM chats_chat_members where chat_members_id = :userId) " +
				"group by chat_id) group by chat_message_id HAVING COUNT(*) < 2 " +
				"AND is_read_by_profiles_id != :profileId")
				.setParameter("userId", userId)
				.setParameter("profileId", profileId)
				.getResultList();
	}

}
