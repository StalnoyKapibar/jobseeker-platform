package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.service.impl.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.ChatService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chatmessages/")
public class ChatMessageRestController {

    @Autowired
    VacancyService vacancyService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatService chatService;

    @GetMapping("{chatId}/last")
    public HttpEntity getAllLastMessages(@PathVariable("chatId") Long id) {
        List<MessageWithDateDTO> lastMessages = chatMessageService.getAllLastMessages();
        Collections.sort(lastMessages);
        return new ResponseEntity(lastMessages, HttpStatus.OK);
    }

    @GetMapping("{chatId}")
    public HttpEntity getAllMessagesInChat(@PathVariable("chatId") Long id) {
        List<ChatMessage> chatMessageList = chatService.getById(id).getChatMessages();
        Collections.sort(chatMessageList);
        return new ResponseEntity(chatMessageList, HttpStatus.OK);
    }

    @PutMapping("change_status/{messageId}")
    public HttpEntity changeStatusMessage(@PathVariable("messageId") Long id, @RequestBody MessageDTO message) {
        Long idMessage = message.getId();
        ChatMessage chatMessage = chatMessageService.getById(idMessage);
        chatMessage.setRead(message.isRead());
        chatMessageService.update(chatMessage);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("count_not_read_messages/admin")
    public HttpEntity getCountNotReadMessagesForAdmin() {
        int count = chatMessageService.getNotReadMessages().size();
        return new ResponseEntity(count, HttpStatus.OK);
    }

    @GetMapping("count_not_read_messages/{chatId}")
    public HttpEntity getCountNotReadMessagesForUser(@PathVariable("chatId") Long chatId) {
        List<ChatMessage> list = new ArrayList<>(chatService.getById(chatId).getChatMessages());
        Long count = list.stream().filter(a -> a.getAuthor().getAuthority().equals(userRoleService.findByAuthority("ROLE_ADMIN"))).filter(a -> a.isRead() == false).count(); //todo переделать на запрос к БД

        return new ResponseEntity(count, HttpStatus.OK);
    }
}
