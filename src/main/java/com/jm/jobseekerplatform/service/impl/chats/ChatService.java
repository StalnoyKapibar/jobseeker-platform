package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatService")
@Transactional
public class ChatService extends AbstractService<Chat> {

    public void addChatMessage(Long chatId, ChatMessage chatMessage) {
        Chat chat = getById(chatId);
        chat.getChatMessages().add(chatMessage); //todo (Nick Dolgopolov) грузятся все сообщения чтобы добавить одно???
        update(chat);
    }
}