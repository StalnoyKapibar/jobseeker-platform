package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.config.ChatAccessHandler;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;


@Controller
public class ChatController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @RequestMapping("/chat/{chatId}")
    public String getChatById(@PathVariable("chatId") Long chatId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getProfile().getId();
        ChatWithTopic chat = chatWithTopicService.getById(chatId);
        if (chat.getClass() == ChatWithTopicVacancy.class) {
            ChatWithTopicVacancy chatWithTopicVacancy = (ChatWithTopicVacancy) chat;
            model.addAttribute("topicName", "вакансия");
            model.addAttribute("topic", chatWithTopicVacancy.getTopic());
            Long seekerId = chatWithTopicVacancy.getCreator().getId();
            Long vacancyId = chatWithTopicVacancy.getTopic().getId();
            Meeting newMeeting = chatWithTopicVacancy.getTopic().getMeetings().stream()
                    .filter(meeting -> meeting.getSeekerProfile().getId().equals(seekerId)
                            && meeting.getVacancy().getId().equals(vacancyId))
                    .findFirst()
                    .orElse(null);
            model.addAttribute("meeting", newMeeting);
            model.addAttribute("isOwner", !userId.equals(seekerId));
        }
        if (user.getClass() == SeekerUser.class) {
            model.addAttribute("seekerProfileId", userId);
        }
        model.addAttribute("profileId", userId);
        model.addAttribute("chatId", chatId);
        return "chat";
    }

    @RequestMapping("/private_chat/{chatId}")
    public String getPrivateChatById(@PathVariable("chatId") Long chatId, Model model,
                                     Principal authentication, HttpServletResponse httpServletResponse) throws IOException {
        Chat chat = chatService.getById(chatId);
        ChatAccessHandler.isContainInChatMembers(chat, userService.findByEmail(authentication.getName()), httpServletResponse);
        if (chat instanceof ChatWithTopic) {
            ChatWithTopic chatWithTopic = (ChatWithTopic) chat;
            model.addAttribute("topicName", chatWithTopic.getTopic().getTypeName());
            model.addAttribute("topic", chatWithTopic.getTopic());
            model.addAttribute("chatWithTopic", chatWithTopic);
        }
        return "private_chat";
    }

    @RequestMapping("/chat/vacancy/{vacancyId:\\d+}")
    public String getChatByVacancyAndAuthenticatedUser(@PathVariable("vacancyId") Long vacancyId,
                                                       Authentication authentication, Model model) {

        User authenticatedUser = ((User) authentication.getPrincipal());
        Profile authenticatedProfile = authenticatedUser.getProfile();

        //ChatWithTopicVacancy chat = chatWithTopicVacancyService.getByTopicIdAndCreatorProfileId(vacancyId, authenticatedProfile.getId());
        //ChatWithTopic<Vacancy> chat = chatWithTopicService.getByTopicIdCreatorProfileIdTopicType(vacancyId, authenticatedProfile.getId(), Vacancy.class);
        ChatWithTopic<Vacancy> chat = chatWithTopicService.getByTopicIdCreatorProfileIdChatType(vacancyId, authenticatedProfile.getId(), ChatWithTopicVacancy.class);

        if (chat == null) {
            chat = new ChatWithTopicVacancy(authenticatedProfile, vacancyService.getById(vacancyId));

            chatWithTopicService.add(chat);
        }

        return "redirect:/chat/" + chat.getId();
    }
}
