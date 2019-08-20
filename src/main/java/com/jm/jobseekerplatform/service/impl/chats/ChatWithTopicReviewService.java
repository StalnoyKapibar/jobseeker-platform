package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.dao.impl.chats.ChatWithTopicReviewDAO;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatWithTopicReviewService")
@Transactional
public class ChatWithTopicReviewService extends ChatWithTopicAbstractService<ChatWithTopicReview> {
    @Autowired
    private ChatWithTopicReviewDAO chatWithTopicReviewDAO;
}
