package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chats/")
public class ChatRestController {

    @Autowired
    VacancyService vacancyService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatService chatService;

    @GetMapping("/last") //todo (Nick Dolgopolov)
    public HttpEntity getAllLastMessages(@PathVariable("chatId") Long id) { //todo Warning:(36, 57) Cannot resolve path variable 'chatId' in request mapping
        List<MessageWithDateDTO> lastMessages = chatMessageService.getAllLastMessages();
        Collections.sort(lastMessages);
        return new ResponseEntity(lastMessages, HttpStatus.OK); //todo Warning:(39, 16) Unchecked call to 'ResponseEntity(T, HttpStatus)' as a member of raw type 'org.springframework.http.ResponseEntity'
    }

    @GetMapping("all")
    public HttpEntity getAllChats() {
        List<Chat> chats = chatService.getAll();
        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov)
        return new ResponseEntity(chats, HttpStatus.OK);
    }


    @GetMapping("{chatId:\\d+}")
    public HttpEntity getAllMessagesInChat(@PathVariable("chatId") Long id) {
        List<ChatMessage> chatMessageList = chatService.getById(id).getChatMessages();
        Collections.sort(chatMessageList);
        return new ResponseEntity(chatMessageList, HttpStatus.OK);
    }

    @PutMapping("update_message")
    public HttpEntity updateMessage(@RequestBody MessageDTO message) {
        ChatMessage chatMessage = chatMessageService.getById(message.getId());
        chatMessage.setRead(message.isRead()); //todo (Nick Dolgopolov) менять всё остальное или переименовать метод
        chatMessageService.update(chatMessage);

        return new ResponseEntity(HttpStatus.OK);
    }

//    @GetMapping("count_not_read_messages/admin") //todo (Nick Dolgopolov)
//    public HttpEntity getCountNotReadMessagesForAdmin() {
//        int count = chatMessageService.getNotReadMessages().size();
//        return new ResponseEntity(count, HttpStatus.OK);
//    }
//
//    @GetMapping("count_not_read_messages/{chatId}") //todo (Nick Dolgopolov)
//    public HttpEntity getCountNotReadMessagesForUser(@PathVariable("chatId") Long chatId) {
//        List<ChatMessage> list = new ArrayList<>(chatService.getById(chatId).getChatMessages());
//        Long count = list.stream().filter(a -> a.getAuthor().getAuthority().equals(userRoleService.findByAuthority("ROLE_ADMIN"))).filter(a -> a.isRead() == false).count(); //todo (Nick Dolgopolov) переделать на запрос к БД
//
//        return new ResponseEntity(count, HttpStatus.OK);
//    }
}
