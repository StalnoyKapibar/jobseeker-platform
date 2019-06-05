package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.ChatMessageService;
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
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable("chatId") String id, ChatMessage chatMessage) {
        Long chatId = Long.parseLong(id);
        String author = chatMessage.getAuthor();
        String adminTo;
        String adminFrom;
        if (author.equals("admin@mail.ru")){
            adminFrom = "false";
            adminTo = "true";
        } else {
            adminFrom = "true";
            adminTo = "false";
        }
        ChatMessage chatMess = new ChatMessage(chatMessage.getText(), author, new Date(), adminFrom, adminTo);
        chatMessageService.add(chatMess);
        Vacancy vacancy = vacancyService.getById(chatId);
        vacancy.getChatMessages().add(chatMess);
        vacancyService.update(vacancy);
        Long idMessage = chatMess.getId();
        ChatMessage message = chatMessageService.getById(idMessage);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
    }


}
