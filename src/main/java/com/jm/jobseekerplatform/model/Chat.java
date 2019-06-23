package com.jm.jobseekerplatform.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
@Inheritance
@Table(name = "chats")
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User createdBy;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = org.hibernate.annotations.FetchMode.SELECT)
    private List<ChatMessage> chatMessages;


    public Chat(){ }

    public Chat(User createdBy){
        this.createdBy = createdBy;
    }


    public Long getId() {
        return id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}
