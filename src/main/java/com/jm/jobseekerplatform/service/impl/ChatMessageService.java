package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.ChatMessageDAO;
import com.jm.jobseekerplatform.dto.LastMessageDTO;
import com.jm.jobseekerplatform.model.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("chatMessageService")
@Transactional
public class ChatMessageService extends AbstractService<ChatMessage> {

    @Autowired
    ChatMessageDAO chatMessageDAO;

    public List<ChatMessage> getNotReadMessages() {
        return chatMessageDAO.getNotReadMessages();
    }

    public List<LastMessageDTO> getAllLastMessages() {
        List<LastMessageDTO> list =  chatMessageDAO.getAllLastMessages();
        for (int i=0; i<list.size(); i++) {
            String author = chatMessageDAO.getById(list.get(i).getId()).getAuthor().getEmail();
            list.get(i).setAuthor(author);
        }
        return list;
    }

}
