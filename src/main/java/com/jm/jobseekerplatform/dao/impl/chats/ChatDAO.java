package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.chats.Chat;
import org.springframework.stereotype.Repository;

@Repository("chatDAO")
public class ChatDAO extends AbstractDAO<Chat> {
//    public void setChatReadByProfileId(Long chatId, Long readerProfileId, Long lastReadMessageId) { //todo (Nick Dolgopolov) оптимизация
//        entityManager.createQuery("update ChatMessage cm set cm.").executeUpdate();
//    }
}