package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatMessageDAO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatMessageService")
@Transactional
public class ChatMessageService extends AbstractService<ChatMessage> {

    @Autowired
    ChatMessageDAO chatMessageDAO;

    public void setMessageReadByProfileId(Long readerProfileId, Long messageId) {
        chatMessageDAO.setMessageReadByProfileId(readerProfileId, messageId);
    }
}