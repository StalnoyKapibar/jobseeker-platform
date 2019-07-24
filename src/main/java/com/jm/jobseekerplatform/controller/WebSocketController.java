package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
        Profile creatorProfile = profileService.getById(messageDTO.getCreatorProfileId());

        ChatMessage chatMessage = new ChatMessage(messageDTO.getText(), creatorProfile, new Date());
        chatMessageService.add(chatMessage);

        chatService.addChatMessage(chatId, chatMessage);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
    }
}
