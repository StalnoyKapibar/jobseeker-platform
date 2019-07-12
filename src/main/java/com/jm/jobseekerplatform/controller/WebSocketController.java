package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    ProfileService profileService;

    @Autowired
    private ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private  ChatMessageService chatMessageService;



    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable("chatId") String id, MessageDTO messageDTO) {
        Long chatId = Long.parseLong(id);
        Profile author = profileService.getById(messageDTO.getAuthor());

        ChatMessage chatMessage = new ChatMessage(messageDTO.getText(), author, new Date(), false);
        chatMessageService.add(chatMessage);

        chatService.addChatMessage(chatId, chatMessage);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
    }
}
