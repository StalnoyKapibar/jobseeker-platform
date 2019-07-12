package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.ChatMessageDAO;
import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("chatMessageService")
@Transactional
public class ChatMessageService extends AbstractService<ChatMessage> {

    @Autowired
    ChatMessageDAO chatMessageDAO;

    public List<ChatMessage> getNotReadMessages() {
        return chatMessageDAO.getNotReadMessages();
    }

    public List<MessageWithDateDTO> getAllLastMessages() {
        List<MessageWithDateDTO> list =  chatMessageDAO.getAllLastMessages();
        for (int i=0; i<list.size(); i++) {
            Long authorId = chatMessageDAO.getById(list.get(i).getId()).getAuthor().getId();
            list.get(i).setAuthor(authorId);
        }
        return list;
    }

}
