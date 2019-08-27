package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ChatAccessHandler {
    private static ChatAccessHandler ourInstance = new ChatAccessHandler();

    public static ChatAccessHandler getInstance() {
        return ourInstance;
    }

    private ChatAccessHandler() {
    }

    public static void isContainInChatMembers(Chat chat, User user, HttpServletResponse httpServletResponse) throws IOException {
        if (!chat.getChatMembers().contains(user)) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
