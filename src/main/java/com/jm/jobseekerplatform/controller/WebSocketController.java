package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.UserService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    VacancyService vacancyService;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    UserService userService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable("chatId") String id, MessageDTO messageDTO) {
        Long chatId = Long.parseLong(id);
        User author = userService.findByEmail(messageDTO.getAuthor());
        ChatMessage chatMessage = new ChatMessage(messageDTO.getText(), author, new Date(), false);
        chatMessageService.add(chatMessage);

        Vacancy vacancy = vacancyService.getById(chatId);
        vacancy.getChatMessages().add(chatMessage);
        vacancyService.update(vacancy);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
    }


}
