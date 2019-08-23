package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("chatService")
@Transactional
public class ChatService extends AbstractService<Chat> {

    public void addChatMessage(Long chatId, ChatMessage chatMessage) {
        Chat chat = getById(chatId);     //todo (Nick Dolgopolov) оптимизировать. грузятся все сообщения чтобы добавить одно???
        chat.getChatMessages().add(chatMessage);
        update(chat);
    }

    public ChatMessage getLastMessage(Long chatId) {
        Chat chat = getById(chatId);
        List<ChatMessage> chatMessages = chat.getChatMessages();     //todo (Nick Dolgopolov) оптимизировать

        if (chatMessages.size() != 0) {
            ChatMessage lastChatMessage = chatMessages.get(0);

            for (int i = 1; i < chatMessages.size(); i++) {
                if (lastChatMessage.getDate().before(chatMessages.get(i).getDate())) {
                    lastChatMessage = chatMessages.get(i);
                }
            }
            return lastChatMessage;
        } else {
            return null;
        }
    }
}