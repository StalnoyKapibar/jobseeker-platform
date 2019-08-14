package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO;
import com.jm.jobseekerplatform.dto.MessageReadDataDTO;
import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
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

    @Autowired
    ChatWithTopicVacancyService chatWithTopicVacancyService;

    @GetMapping("/last")
    public HttpEntity getAllLastMessages() { //todo (Nick Dolgopolov)
        List<MessageWithDateDTO> lastMessages = chatMessageService.getAllLastMessages();
        Collections.sort(lastMessages);
        return new ResponseEntity(lastMessages, HttpStatus.OK);
    }

    @GetMapping("all")
    public HttpEntity getAllChats() {
        List<ChatWithTopicVacancy> chats = chatWithTopicVacancyService.getAll();

        List<ChatInfoWithTopicDTO> chatsInfo = getChatInfoDTOs(chats);

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov)
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    private List<ChatInfoWithTopicDTO> getChatInfoDTOs(Collection<ChatWithTopicVacancy> chats) {
        List<ChatInfoWithTopicDTO> chatsInfo = new ArrayList<>();

        for (ChatWithTopicVacancy chat : chats) {
            ChatInfoWithTopicDTO chatInfoWithTopicDTO = new ChatInfoWithTopicDTO(chat);
            chatsInfo.add(chatInfoWithTopicDTO);
        }
        return chatsInfo;
    }

    @GetMapping("getAllChatsByProfileId/{profileId:\\d+}")
    public HttpEntity getAllChatsByProfileId(@PathVariable("profileId") Long profileId) {

        List<ChatInfoDetailWithTopicDTO> chatsInfo = chatWithTopicVacancyService.getAllChatsInfoDTOByProfileId(profileId);

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov)
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("getAllUnreadChatsByProfileId/{profileId:\\d+}")
    public HttpEntity getAllUnreadChatsByProfileId(@PathVariable("profileId") Long profileId) {

        //todo (Nick Dolgopolov)
        Set<ChatWithTopicVacancy> chats = new HashSet<>(chatWithTopicVacancyService.getAllUnreadChatsByProfileId(profileId));

        return new ResponseEntity(chats, HttpStatus.OK);
    }

    @GetMapping("getAllUnreadChatsByAuthProfileId")
    public HttpEntity getAllUnreadChatsByAuthProfileId(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return getAllUnreadChatsByProfileId(user.getProfile().getId());
    }

    @GetMapping("getCountOfChatsByAuthProfileId")
    public HttpEntity getCountOfChatsByAuthProfileId(Authentication authentication) {

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

        for (int i = chatMessageList.size() - 1; i >= 0; i--) {
            ChatMessage chatMessage = chatMessageList.get(i);
            if (/*chatMessage.getId() <= messageReadDataDTO.getLastReadMessageId() &&*/ //todo (Nick Dolgopolov) по id или надо по дате?
                    !chatMessage.getCreatorProfile().getId().equals(messageReadDataDTO.getReaderProfileId()) &&
                    !chatMessage.getIsReadByProfilesId().contains(messageReadDataDTO.getReaderProfileId())) {
                chatMessage.getIsReadByProfilesId().add(messageReadDataDTO.getReaderProfileId());

                chatMessageService.update(chatMessage);
            }
        } //todo (Nick Dolgopolov) запросом + фильтр

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
