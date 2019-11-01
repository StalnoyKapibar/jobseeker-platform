/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.NewChatDAO;
import com.jm.jobseekerplatform.model.chats.NewChat;
import com.jm.jobseekerplatform.model.chats.NewChatMessage;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

public class NewChatService extends AbstractService<NewChat> {
    @Autowired
    private NewChatDAO chatDAO;

    @Autowired
    private Authentication authentication;

    public void addChatMessage(Long chatId, NewChatMessage chatMessage) {
        User user = (User) authentication.getPrincipal();
        user.getId();
    }

}
