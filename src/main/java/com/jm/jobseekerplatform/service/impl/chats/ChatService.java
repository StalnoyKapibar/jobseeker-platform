package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.interfaces.chats.ChatDao;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service("chatService")
@Transactional
public class ChatService extends AbstractService<Chat> {

    @Autowired
    private ChatDao chatDAO;

    public void addChatMessage(Long chatId, ChatMessage chatMessage) {
        Chat chat = getById(chatId);
        chat.getChatMessages().add(chatMessage);
        update(chat);
    }

    public void setChatReadByProfileId(Long chatId, Long readerProfileId, Long lastReadMessageId) {
        chatDAO.setChatReadByProfileId(chatId, readerProfileId, lastReadMessageId);
    }

    public List<BigInteger> getChatMembersIds(Long chatId) {
        return chatDAO.getChatMembersIds(chatId);
    }

    public List<BigInteger> getProfileIDByChat(Long chatId) {
        return chatDAO.getProfileIDByChatDAO(chatId);
    }

    public List<Object> getAllIdLastMessagesByUserId(Long userId, Long profileId) {
        return chatDAO.getAllIdLastMessagesByUserIdDAO(userId, profileId);
    }

}
