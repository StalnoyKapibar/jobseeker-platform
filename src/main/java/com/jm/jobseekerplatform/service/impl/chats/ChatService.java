package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatDAO;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatService")
@Transactional
public class ChatService extends AbstractService<Chat> {

    @Autowired
    ChatDAO chatDAO;

    public void addChatMessage(Long chatId, ChatMessage chatMessage) {
        Chat chat = getById(chatId);     //todo (Nick Dolgopolov) хранимые процедуры
        chat.getChatMessages().add(chatMessage);
        update(chat);
    }

    public void setChatReadByProfileId(Long chatId, Long readerProfileId, Long lastReadMessageId) {
        chatDAO.setChatReadByProfileId(chatId, readerProfileId, lastReadMessageId);
    }
}