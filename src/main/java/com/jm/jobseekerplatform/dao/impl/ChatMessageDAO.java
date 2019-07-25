package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.MessageWithDateDTO;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("chatMessageDAO")
public class ChatMessageDAO extends AbstractDAO<ChatMessage> {

    public List<ChatMessage> getNotReadMessages() {
        return null; //todo (Nick Dolgopolov)
//        List<ChatMessage> list = new ArrayList<>();
//        list.addAll(entityManager
//                .createQuery("select c from ChatMessage c join c.author u join u.authority a where a.authority in (:param) and c.isRead in (:isread)", ChatMessage.class)
//                .setParameter("isread", false)
//                .setParameter("param", "ROLE_EMPLOYER").getResultList());
//        return list;
    }

    public List<MessageWithDateDTO> getAllLastMessages() {
        return null; //todo (Nick Dolgopolov)
//        List<MessageWithDateDTO> list = new ArrayList();
//        List query = entityManager.unwrap(Session.class).createSQLQuery("select c.text as lastMessage, c.date, c.isread as isRead, chatId, vacancyHeadline, c.id from chatmessages as c inner join (select vc.vacancy_id as chatId, (select vacancies.headline from vacancies where vacancies.id=vc.vacancy_id) as vacancyHeadline, max(vc.chat_messages_id) as id from vacancies_chat_messages as vc group by vc.vacancy_id) as a on c.id=a.id")
//                .addScalar("lastMessage", new StringType())
//                .addScalar("date", new DateType())
//                .addScalar("isRead", new BooleanType())
//                .addScalar("chatId", new LongType())
//                .addScalar("vacancyHeadline", new StringType())
//                .addScalar("id", new LongType()).getResultList();
//        for (int i=0; i<query.size(); i++) {
//            MessageWithDateDTO message = new MessageWithDateDTO(
//                    (Long)((Object[])query.get(i))[5],
//                    (String)((Object[])query.get(i))[0],
//                    (Date)((Object[])query.get(i))[1],
//                    (boolean)((Object[])query.get(i))[2]);
//
//            list.add(message);
//        }
//        return list;
    }
}
