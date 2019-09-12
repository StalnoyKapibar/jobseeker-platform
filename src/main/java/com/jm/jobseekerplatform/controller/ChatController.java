package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.config.ChatAccessHandler;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatWithTopic;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicResume;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private EmployerUserService employerUserService;

    @RequestMapping("/chat/{chatId}")
    public String getChatById(@PathVariable("chatId") Long chatId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Long currentProfileId = user.getProfile().getId();
        ChatWithTopic chatWithTopic = chatWithTopicService.getById(chatId);

        model.addAttribute("profileId", currentProfileId);
        model.addAttribute("chatId", chatWithTopic.getId());

        model.addAttribute("topicName", chatWithTopic.getTopic().getTypeName());
        model.addAttribute("topic", chatWithTopic.getTopic());

        return "chats/chat";
    }

    @RequestMapping("/chat/seeker-vacancy-employer/{chatId}")
    public String getChatSeekerVacancyEmployerById(@PathVariable("chatId") Long chatId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Long currentProfileId = user.getProfile().getId();
        ChatWithTopic chatWithTopic = chatWithTopicService.getById(chatId);

        model.addAttribute("profileId", currentProfileId);
        model.addAttribute("chatId", chatWithTopic.getId());

        model.addAttribute("topicName", chatWithTopic.getTopic().getTypeName());
        model.addAttribute("topic", chatWithTopic.getTopic());

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

    @RequestMapping("/private_chat/{chatId}")
    public String getPrivateChatById(@PathVariable("chatId") Long chatId, Model model,
                                     Authentication authentication, HttpServletResponse httpServletResponse) throws IOException {
        Chat chat = chatService.getById(chatId);
        User user = (User) authentication.getPrincipal();
        ChatAccessHandler.isContainInChatMembers(chat, userService.findByEmail(user.getUsername()), httpServletResponse);
        model.addAttribute("profileId", user.getProfile().getId());
        if (user.getAuthority().toString().equals("ROLE_EMPLOYER")) {
            model.addAttribute("employerProfileId", user.getProfile().getId());
        } else if (user.getAuthority().toString().equals("ROLE_SEEKER")) {
            model.addAttribute("seekerProfileId", user.getProfile().getId());
        } else {
            model.addAttribute("adminProfileId", user.getProfile().getId());
        }
        if (chat instanceof ChatWithTopic) {
            ChatWithTopic chatWithTopic = (ChatWithTopic) chat;
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
        ChatWithTopic<Vacancy> chat = chatWithTopicService.getChatByTopicIdCreatorProfileIdChatType(vacancyId, authenticatedProfile.getId(), ChatWithTopicVacancy.class);

        if (chat == null) {
            Vacancy vacancy = vacancyService.getById(vacancyId);
            EmployerProfile employerProfile = vacancy.getCreatorProfile();
            List<User> chatMembers = new ArrayList<>();
            chatMembers.add(authenticatedUser);
            chatMembers.add(employerUserService.getByProfileId(employerProfile.getId()));
            chat = new ChatWithTopicVacancy(authenticatedProfile, chatMembers, vacancy);
            chatWithTopicService.add(chat);
        }

        return "redirect:/chat/" + chat.getId();
    }

    @RequestMapping("chat/resume/{resumeId}")
    public String getChatByResumeAndAuthenticatedUser(@PathVariable("resumeId") Long resumeId,
                                                      Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Profile authenticatedProfile = authenticatedUser.getProfile();

        ChatWithTopic<Resume> chat = chatWithTopicService.getChatByTopicIdCreatorProfileIdChatType(resumeId, authenticatedProfile.getId(), ChatWithTopicResume.class);
        if (chat == null) {
            Resume resume = resumeService.getById(resumeId);
            SeekerProfile seekerProfile = (SeekerProfile) Hibernate.unproxy(resume.getCreatorProfile());
            List<User> chatMembers = new ArrayList<>();
            chatMembers.add(authenticatedUser);
            chatMembers.add(seekerUserService.getByProfileId(seekerProfile.getId()));
            chat = new ChatWithTopicResume(authenticatedProfile, chatMembers, resume);
            chatWithTopicService.add(chat);
        }
        return "redirect:/private_chat/" + chat.getId();
    }
}
