package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.*;
import com.jm.jobseekerplatform.model.profiles.Profile;
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
    @JsonProperty("creatorProfile")
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private Profile creatorProfile;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = org.hibernate.annotations.FetchMode.SELECT)
    @JsonIgnore
    private List<ChatMessage> chatMessages;


    public Chat(){ }

    public Chat(Profile creatorProfile){
        this.creatorProfile = creatorProfile;
    }


    public Long getId() {
        return id;
    }

    public Profile getCreator() {
        return creatorProfile;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}
