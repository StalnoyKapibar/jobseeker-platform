package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.MessageReadDataDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    ChatWithTopicVacancyService chatWithTopicVacancyService;

    @GetMapping("all")
    public HttpEntity getAllChats() {
        List<ChatInfoWithTopicDTO> chatsInfo = chatWithTopicVacancyService.getAllChatsInfoDTO();

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov) как организовывать сортировку?
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("getAllChatsByProfileId/{profileId:\\d+}")
    public HttpEntity getAllChatsByProfileId(@PathVariable("profileId") Long profileId) {

        List<ChatInfoDetailWithTopicDTO> chatsInfo = chatWithTopicVacancyService.getAllChatsInfoDTOByProfileId(profileId);

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov) как организовывать сортировку?
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("getCountOfUnreadChatsByProfileId")
    public HttpEntity getCountOfUnreadChatsByProfileId(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        long countOfUnreadChatsByProfileId = chatWithTopicVacancyService.getCountOfUnreadChatsByProfileId(user.getProfile().getId());

        return new ResponseEntity(countOfUnreadChatsByProfileId, HttpStatus.OK);
    }


    @GetMapping("{chatId:\\d+}")
    public HttpEntity getAllMessagesInChat(@PathVariable("chatId") Long chatId) {
        List<ChatMessage> chatMessageList = chatService.getById(chatId).getChatMessages();
        Collections.sort(chatMessageList);
        return new ResponseEntity(chatMessageList, HttpStatus.OK);
    }

    @PutMapping("set_chat_read_by_profile_id")
    public HttpEntity setChatReadByProfileId(@RequestBody MessageReadDataDTO messageReadDataDTO) {

        List<ChatMessage> chatMessageList = chatService.getById(messageReadDataDTO.getChatId()).getChatMessages();

        for (int i = chatMessageList.size() - 1; i >= 0; i--) { //todo (Nick Dolgopolov) переделать на зарос, который будет в базе менять статус только у нужных сообщений (фильтр)
            ChatMessage chatMessage = chatMessageList.get(i);
            if (/*chatMessage.getId() <= messageReadDataDTO.getLastReadMessageId() &&*/ //todo (Nick Dolgopolov) по id или надо по дате?
                    !chatMessage.getCreatorProfile().getId().equals(messageReadDataDTO.getReaderProfileId()) &&
                            !chatMessage.getIsReadByProfilesId().contains(messageReadDataDTO.getReaderProfileId())) {
                chatMessage.getIsReadByProfilesId().add(messageReadDataDTO.getReaderProfileId());

                chatMessageService.update(chatMessage);
            }
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("set_message_read_by_profile_id")
    public HttpEntity setMessageReadByProfileId(@RequestBody MessageReadDataDTO messageReadDataDTO) {

        ChatMessage chatMessage = chatMessageService.getById(messageReadDataDTO.getLastReadMessageId());

        if (!chatMessage.getCreatorProfile().getId().equals(messageReadDataDTO.getReaderProfileId()) &&
                !chatMessage.getIsReadByProfilesId().contains(messageReadDataDTO.getReaderProfileId())) {

            chatMessage.getIsReadByProfilesId().add(messageReadDataDTO.getReaderProfileId());
            chatMessageService.update(chatMessage);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
