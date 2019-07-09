package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.users.User;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private User createdBy;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = org.hibernate.annotations.FetchMode.SELECT)
    @JsonIgnore
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
