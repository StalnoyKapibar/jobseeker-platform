package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.MessageDTO;
import com.jm.jobseekerplatform.dto.MessageReadDataDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoDetailWithTopicDTO;
import com.jm.jobseekerplatform.dto.chatInfo.ChatInfoWithTopicDTO;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicReview;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.EmployerReviewsService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.AdminUserService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    VacancyService vacancyService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatService chatService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private EmployerReviewsService employerReviewsService;

    @Autowired
    private SeekerUserService seekerUserService;

    @GetMapping("all")
    public HttpEntity getAllChats() {
        List<ChatInfoWithTopicDTO> chatsInfo = chatWithTopicService.getAllChatsInfoDTO();
        return new ResponseEntity<>(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("getAllChatsByProfileId/{profileId:\\d+}")
    public HttpEntity getAllChatsByProfileId(@PathVariable("profileId") Long profileId) {

        List<ChatInfoDetailWithTopicDTO> chatsInfo = chatWithTopicService.getAllChatsInfoDTOByProfileId(profileId);

        return new ResponseEntity<>(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("getCountOfUnreadChatsByProfileId")
    public HttpEntity getCountOfUnreadChatsByProfileId(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        long countOfUnreadChatsByProfileId = chatWithTopicService.getCountOfUnreadChatsByProfileId(user.getProfile().getId());

        return new ResponseEntity<>(countOfUnreadChatsByProfileId, HttpStatus.OK);
    }

    @PostMapping
    public String saveMeeting(@RequestParam("vacancyId") Long vacancyId, @RequestParam("seekerId") Long seekerId) {
        SeekerProfile seeker = seekerProfileService.getById(seekerId);
        ChatWithTopic<Vacancy> chat = chatWithTopicService.getChatByTopicIdCreatorProfileIdChatType(vacancyId, seeker.getId(), ChatWithTopicVacancy.class);
        if (chat == null) {
            Vacancy vacancy = vacancyService.getById(vacancyId);
            EmployerProfile employerProfile = vacancy.getCreatorProfile();
            List<User> chatMembers = new ArrayList<>();
            chatMembers.add(seekerUserService.getByProfileId(seeker.getId()));
            chatMembers.add(employerUserService.getByProfileId(employerProfile.getId()));
            chat = new ChatWithTopicVacancy(seeker, chatMembers, vacancy);
            chatWithTopicService.add(chat);
        }
        return chat.getId().toString();
    }

    @PostMapping("/add_complain_chat")
    public ResponseEntity<String> addComplainChat(@RequestBody MessageDTO message,
                                                  @RequestParam("reviewId") Long reviewId,
                                                  @RequestParam("employerProfileId") Long employerProfileId) {
        ChatMessage chatMessage = new ChatMessage(message.getText(),
                employerProfileService.getById(employerProfileId), message.getDate());
        List<User> list = new ArrayList<>();
        list.add(employerUserService.getByProfileId(employerProfileId));
        list.add(adminUserService.getByProfileId(1L));
        ChatWithTopicReview chatWithTopic = chatWithTopicService.getChatByTopicIdCreatorProfileIdChatType(reviewId,
                employerProfileId, ChatWithTopicReview.class);
        if (chatWithTopic == null) {
            chatWithTopic = new ChatWithTopicReview(employerProfileService.getById(employerProfileId),
                    list, employerReviewsService.getById(reviewId));
            chatWithTopicService.add(chatWithTopic);
        }
        chatMessageService.add(chatMessage);
        chatService.addChatMessage(chatWithTopic.getId(), chatMessage);
        return new ResponseEntity<>(chatWithTopic.getId().toString(), HttpStatus.OK);
    }

    @GetMapping("{chatId:\\d+}")
    public HttpEntity getAllMessagesInChat(@PathVariable("chatId") Long chatId) {
        List<ChatMessage> chatMessageList = chatService.getById(chatId).getChatMessages();
        Collections.sort(chatMessageList);
        return new ResponseEntity<>(chatMessageList, HttpStatus.OK);
    }

    @PutMapping("set_chat_read_by_profile_id")
    public HttpEntity setChatReadByProfileId(@RequestBody MessageReadDataDTO messageReadDataDTO) {

        chatService.setChatReadByProfileId(messageReadDataDTO.getChatId(), messageReadDataDTO.getReaderProfileId(), messageReadDataDTO.getMessageId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("set_message_read_by_profile_id")
    public HttpEntity setMessageReadByProfileId(@RequestBody MessageReadDataDTO messageReadDataDTO) {

        chatMessageService.setMessageReadByProfileId(messageReadDataDTO.getReaderProfileId(), messageReadDataDTO.getMessageId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("getChatByMessageId/{messageId}")
    public HttpEntity getChatByMessageId(@PathVariable("messageId") Long messsageId) {
        ChatWithTopic chatWithTopic= chatWithTopicService.getChatByMessageId(messsageId);
        return new ResponseEntity<>(chatWithTopic, HttpStatus.OK);
    }
}