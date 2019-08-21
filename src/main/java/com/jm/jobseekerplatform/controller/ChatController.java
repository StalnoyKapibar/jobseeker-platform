package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.config.ChatAccessHandler;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
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
        Long currentProfileId = user.getProfile().getId();
        ChatWithTopic chatWithTopic = chatWithTopicService.getById(chatId);

        model.addAttribute("profileId", currentProfileId);
        model.addAttribute("chatId", chatId);

        model.addAttribute("topicName", chatWithTopic.getTopic().getTypeName());
        model.addAttribute("topic", chatWithTopic.getTopic());

        if (chatWithTopic.getClass() == ChatWithTopicVacancy.class &&
                (user.getProfile().getClass() == SeekerProfile.class ||
                        chatWithTopic.getTopic().getCreatorProfile().getId().equals(user.getProfile().getId()))) {

            Long seekerId = chatWithTopic.getCreator().getId();
            Long vacancyId = chatWithTopic.getTopic().getId();

            ChatWithTopicVacancy chatWithTopicVacancy = (ChatWithTopicVacancy) chatWithTopic;

            Meeting newMeeting = chatWithTopicVacancy.getTopic().getMeetings().stream()
                    .filter(meeting -> meeting.getSeekerProfile().getId().equals(seekerId)
                            && meeting.getVacancy().getId().equals(vacancyId))
                    .findFirst()
                    .orElse(null);

            model.addAttribute("meeting", newMeeting);
            model.addAttribute("isOwner", !currentProfileId.equals(seekerId));

            return "chats/chat-seeker-vacancy-employer";
        }

        return "chats/chat";
    }


    @RequestMapping("/private_chat/{chatId}")
    public String getPrivateChatById(@PathVariable("chatId") Long chatId, Model model,
                                     Authentication authentication, HttpServletResponse httpServletResponse) throws IOException {
        Chat chat = chatService.getById(chatId);
        User user = (User) authentication.getPrincipal();
        ChatAccessHandler.isContainInChatMembers(chat, userService.findByEmail(user.getUsername()), httpServletResponse);
        model.addAttribute("profileId", user.getProfile().getId());
        model.addAttribute("employerProfileId", user.getProfile().getId());
        if (chat instanceof ChatWithTopic) {
            ChatWithTopic chatWithTopic = (ChatWithTopic) chat;
            model.addAttribute("topicName", chatWithTopic.getTopic().getTypeName());
            model.addAttribute("topic", chatWithTopic.getTopic());
            model.addAttribute("chatWithTopic", chatWithTopic);
        }
        return "chats/private_chat";
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
