/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.*;
import com.jm.jobseekerplatform.model.profiles.Profile;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance
@Table(name = "new_chat")
public class NewChat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    //  @ManyToOne
    @JsonProperty("creatorProfile")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Profile creatorProfile;

    @OneToMany
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    @JsonIgnore
    private List<NewChatMember> chatMembers;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = org.hibernate.annotations.FetchMode.SELECT)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    @JsonIgnore
    private List<NewChatMessage> chatMessages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public Profile getCreatorProfile() {
        return creatorProfile;
    }

    public void setCreatorProfile(Profile creatorProfile) {
        // создателя чата сразу же добавляем в участники этого чата
        NewChatMember newChatMember = new NewChatMember(creatorProfile, this.id, new Date());
        this.chatMembers.add(newChatMember);
        this.creatorProfile = creatorProfile;
    }

    public List<NewChatMember> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(List<NewChatMember> chatMembers) {
        this.chatMembers = chatMembers;
    }

    public List<NewChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<NewChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void addChatMessage(String message, Profile mOwner) {
        NewChatMessage chatMessage = new NewChatMessage(this.id, mOwner, message, new Date());
        this.chatMessages.add(chatMessage);
    }

    public NewChat() {
    }

}

enum ReferenceType {
    RESUME,
    VACANCY,
    REPLY
}