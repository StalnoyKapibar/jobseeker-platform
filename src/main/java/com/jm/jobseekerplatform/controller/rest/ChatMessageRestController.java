package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.ChatMessageService;
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
    ChatMessageService chatMessageService;

    @GetMapping("last")
    public HttpEntity getAllLastMessages() {
        List<Vacancy> vacancies = vacancyService.getAll();

        List<MessageDTO> lastMessages = new ArrayList<>();

        for (int i=0; i<vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            Set<ChatMessage> chatMessages = vacancy.getChatMessages();

            if (chatMessages!=null && chatMessages.size()!=0) {
                List<ChatMessage> list = new ArrayList<>(chatMessages);
                Collections.sort(list);
                MessageDTO messageDTO = new MessageDTO(vacancy.getId(), vacancy.getHeadline(), list.get(0).getText(), list.get(0).getCreateDate(), list.get(0).getAdminTo());
                lastMessages.add(messageDTO);
            }
        }
        Collections.sort(lastMessages);

        return new ResponseEntity(lastMessages, HttpStatus.OK);
    }

    @GetMapping("{vacancyId}")
    public HttpEntity getAllMessages(@PathVariable("vacancyId") Long id) {
        Set<ChatMessage> chatMessageModel = vacancyService.getById(id).getChatMessages();
        List<ChatMessage> list = new ArrayList<>(chatMessageModel);
        Collections.sort(list);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutMapping("{messageId}")
    public HttpEntity changeStatusMessage(@PathVariable("messageId") Long id, @RequestBody ChatMessage message) {

        //ChatMessage chatMessage = chatMessageService.getById(id);
        System.out.println(message);
        chatMessageService.update(message);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("count_not_read_messages/admin")
    public HttpEntity getCountNotReadMessagesForAdmin() {
        List<ChatMessage> list = chatMessageService.getAll();
        int count = 0;
        for (int i =0; i<list.size(); i++) {
            String s = list.get(i).getAdminTo();
            if (s.equals("false")) {
                count++;
            }
        }
        return new ResponseEntity(count, HttpStatus.OK);
    }

    @GetMapping("count_not_read_messages/{vacancyId}")
    public HttpEntity getCountNotReadMessagesForUser(@PathVariable("vacancyId") Long id) {
        List<ChatMessage> list = new ArrayList<>(vacancyService.getById(id).getChatMessages());
        int count = 0;
        for (int i =0; i<list.size(); i++) {
            String s = list.get(i).getAdminFrom();
            if (s.equals("false")) {
                count++;
            }
        }
        return new ResponseEntity(count, HttpStatus.OK);
    }
}
