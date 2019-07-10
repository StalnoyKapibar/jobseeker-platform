package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.LastMessageDTO;
import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.service.impl.ChatMessageService;
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

    @GetMapping("last")
    public HttpEntity getAllLastMessages() {
        List<LastMessageDTO> lastMessages = chatMessageService.getAllLastMessages();
        Collections.sort(lastMessages);
        return new ResponseEntity(lastMessages, HttpStatus.OK);
    }

    @GetMapping("{vacancyId}")
    public HttpEntity getAllMessages(@PathVariable("vacancyId") Long id) {
        List<ChatMessage> chatMessageList = vacancyService.getById(id).getChatMessages();
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

    @GetMapping("count_not_read_messages/{vacancyId}")
    public HttpEntity getCountNotReadMessagesForUser(@PathVariable("vacancyId") Long id) {
        List<ChatMessage> list = new ArrayList<>(vacancyService.getById(id).getChatMessages());
        Long count = list.stream().filter(a -> a.getAuthor().getAuthority().equals(userRoleService.findByAuthority("ROLE_ADMIN"))).filter(a -> a.isRead() == false).count();

        return new ResponseEntity(count, HttpStatus.OK);
    }
}
