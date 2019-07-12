package com.jm.jobseekerplatform.service.impl.chats;

import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatWithTopicVacancyService")
@Transactional
public class ChatWithTopicVacancyService extends AbstractService<ChatWithTopicVacancy> {
    public ChatWithTopicVacancy getByVacancyIdAndCreatorProfileId(String vacancyId, Long profileId) {

        ChatWithTopicVacancy chat = null; //todo

        return chat;
    }
}