package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository("chatMessageDAO")
public class ChatMessageDAO extends AbstractDAO<ChatMessage> {
}
