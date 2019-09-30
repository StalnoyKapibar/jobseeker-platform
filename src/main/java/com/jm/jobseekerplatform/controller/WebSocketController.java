package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    ProfileService profileService;

    @Autowired
    private ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable("chatId") String id, MessageDTO messageDTO,
                            Authentication authentication) {
        Long chatId = Long.parseLong(id);
        List<BigInteger> membersId = chatService.getChatMembers(chatId);
        User user = (User) authentication.getPrincipal();
        if (membersId.contains(BigInteger.valueOf(user.getId()))) {
            Profile creatorProfile = profileService.getById(messageDTO.getCreatorProfileId());
            ChatMessage chatMessage = new ChatMessage(messageDTO.getText(), creatorProfile, new Date());
            chatMessageService.add(chatMessage);
            chatService.addChatMessage(chatId, chatMessage);
            simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
        }
    }

    @MessageMapping("/private_chat/{chatId}")
    public void sendPrivateMessage(@DestinationVariable("chatId") String chatId, MessageDTO messageDTO) {
        Profile creatorProfile = profileService.getById(messageDTO.getCreatorProfileId());
        ChatMessage chatMessage = new ChatMessage(messageDTO.getText(), creatorProfile, new Date());
        chatMessageService.add(chatMessage);
        chatService.addChatMessage(Long.parseLong(chatId), chatMessage);
        List<User> userList = chatService.getById(Long.parseLong(chatId)).getChatMembers();
        for (User user : userList) {
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/reply", chatMessage);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
