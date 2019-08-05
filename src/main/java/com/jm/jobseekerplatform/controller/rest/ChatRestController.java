package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.ChatInfoDTO;
import com.jm.jobseekerplatform.dto.MessageReadDataDTO;
import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Status;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicVacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    @Autowired
    private ChatWithTopicService chatWithTopicService;

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
    ChatWithTopicVacancyService chatWithTopicVacancyService;

    @GetMapping("/last") //todo (Nick Dolgopolov)
    public HttpEntity getAllLastMessages(@PathVariable("chatId") Long id) { //todo Warning:(36, 57) Cannot resolve path variable 'chatId' in request mapping
        List<MessageWithDateDTO> lastMessages = chatMessageService.getAllLastMessages();
        Collections.sort(lastMessages);
        return new ResponseEntity(lastMessages, HttpStatus.OK); //todo Warning:(39, 16) Unchecked call to 'ResponseEntity(T, HttpStatus)' as a member of raw type 'org.springframework.http.ResponseEntity'
    }

    @GetMapping("all")
    public HttpEntity getAllChats() {
        List<ChatWithTopicVacancy> chats = chatWithTopicVacancyService.getAll();

        List<ChatInfoDTO> chatsInfo = getChatInfoDTOs(chats);

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov)
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    @GetMapping("my/{profileId:\\d+}")
    public HttpEntity getAllChatsByProfileId(@PathVariable("profileId") Long profileId) {

        Set<ChatWithTopicVacancy> chats = new HashSet<>();

        chats.addAll(chatWithTopicVacancyService.getAllByChatCreatorProfileId(profileId)); //todo (Nick Dolgopolov) сделать один запрос
        chats.addAll(chatWithTopicVacancyService.getAllByParticipantProfileId(profileId));
        chats.addAll(chatWithTopicVacancyService.getAllChatsByTopicCreatorProfileId(profileId));

        List<ChatInfoDTO> chatsInfo = getChatInfoDTOs(chats);

        //Collections.sort(chats, (o1, o2) -> ...); //todo (Nick Dolgopolov)
        return new ResponseEntity(chatsInfo, HttpStatus.OK);
    }

    private List<ChatInfoDTO> getChatInfoDTOs(Collection<ChatWithTopicVacancy> chats) {
        List<ChatInfoDTO> chatsInfo = new ArrayList<>();

        for (ChatWithTopicVacancy chat : chats) {
            ChatInfoDTO chatInfoDTO = ChatInfoDTO.fromChatWithTopic(chat);
            chatInfoDTO.setLastMessage(chatService.getLastMessage(chat.getId()));
            chatsInfo.add(chatInfoDTO);
        }
        return chatsInfo;
    }

    @PostMapping(params = {"vacancyId","seekerId"})
    public String saveMeeting(@RequestParam("vacancyId") Long vacancyId,
                            @RequestParam("seekerId") Long seekerId){
        SeekerProfile seeker = seekerProfileService.getById(seekerId);
        ChatWithTopic<Vacancy> chat = chatWithTopicService.getByTopicIdCreatorProfileIdChatType(vacancyId, seeker.getId(), ChatWithTopicVacancy.class);
        if (chat == null) {
            chat = new ChatWithTopicVacancy(seeker, vacancyService.getById(vacancyId));
            chatWithTopicService.add(chat);
        }
        return chat.getId().toString();
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
            if (chatMessage.getId() <= messageReadDataDTO.getLastReadMessageId() && //todo (Nick Dolgopolov) по id или надо по дате?
                    chatMessage.getCreatorProfile().getId() != messageReadDataDTO.getReaderProfileId() &&
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

        if (chatMessage.getCreatorProfile().getId() != messageReadDataDTO.getReaderProfileId() &&
                !chatMessage.getIsReadByProfilesId().contains(messageReadDataDTO.getReaderProfileId())) {

            chatMessage.getIsReadByProfilesId().add(messageReadDataDTO.getReaderProfileId());
            chatMessageService.update(chatMessage);
        }

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
