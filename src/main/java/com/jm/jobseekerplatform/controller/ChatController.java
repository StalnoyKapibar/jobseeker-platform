package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ChatController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @RequestMapping("/chat/{chatId}")
    public String getChatById(@PathVariable("chatId") Long chatId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Long currentProfileId = user.getProfile().getId();
        ChatWithTopic chatWithTopic = chatWithTopicService.getById(chatId);

        PrepareModelForChat(chatId, model, currentProfileId, chatWithTopic);

        return "chats/chat";
    }

    @RequestMapping("/chat/seeker-vacancy-employer/{chatId}")
    public String getChatSeekerVacancyEmployerById(@PathVariable("chatId") Long chatId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Long currentProfileId = user.getProfile().getId();
        ChatWithTopic chatWithTopic = chatWithTopicService.getById(chatId);

        PrepareModelForChat(chatId, model, currentProfileId, chatWithTopic);

        Long seekerId = chatWithTopic.getCreatorProfile().getId();
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

    private void PrepareModelForChat(Long chatId, Model model, Long currentProfileId, ChatWithTopic chatWithTopic) {
        model.addAttribute("profileId", currentProfileId);
        model.addAttribute("chatId", chatWithTopic.getId());

        model.addAttribute("topic", chatWithTopic.getTopic());
    }

    @RequestMapping("/chat/vacancy/{vacancyId:\\d+}")
    public String getChatByVacancyAndAuthenticatedUser(@PathVariable("vacancyId") Long vacancyId,
                                                       Authentication authentication, Model model) {

        User authenticatedUser = ((User) authentication.getPrincipal());
        Profile authenticatedProfile = authenticatedUser.getProfile();

        //ChatWithTopicVacancy chat = chatWithTopicVacancyService.getByTopicIdAndCreatorProfileId(vacancyId, authenticatedProfile.getId());
        //ChatWithTopic<Vacancy> chat = chatWithTopicService.getByTopicIdCreatorProfileIdTopicType(vacancyId, authenticatedProfile.getId(), Vacancy.class);
        ChatWithTopic<Vacancy> chat = chatWithTopicService.getChatByTopicIdCreatorProfileIdChatType(vacancyId, authenticatedProfile.getId(), ChatWithTopicVacancy.class);

        if (chat == null) {
            chat = new ChatWithTopicVacancy(authenticatedProfile, vacancyService.getById(vacancyId));

            chatWithTopicService.add(chat);
        }

        return "redirect:/chat/" + chat.getId();
    }
}
