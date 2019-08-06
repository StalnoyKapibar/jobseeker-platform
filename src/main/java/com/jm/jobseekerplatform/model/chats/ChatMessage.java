package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "chatmessages")
public class ChatMessage implements Serializable, Comparable<ChatMessage> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private Profile creatorProfile;

    @Column(name = "date")
    private Date date;

    @Column
    @ElementCollection
    private Set<Long> isReadByProfilesId;

    public ChatMessage() {
    }

    public ChatMessage(String text, Profile creatorProfile, Date date) {
        this.text = text;
        this.creatorProfile = creatorProfile;
        this.date = date;
        this.isReadByProfilesId = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    public Profile getCreatorProfile() {
        return creatorProfile;
    }

    public void setCreatorProfile(Profile creatorProfile) {
        this.creatorProfile = creatorProfile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Long> getIsReadByProfilesId() {
        return isReadByProfilesId;
    }

    public void setIsReadByProfilesId(Set<Long> isReadByProfilesId) {
        this.isReadByProfilesId = isReadByProfilesId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ",\"text\":\"" + text + '\"' +
                ",\"author type\":\"" + creatorProfile.getClass() + '\"' +
                ",\"author id\":\"" + creatorProfile.getId() + '\"' +
                ",\"createDate\":\"" + date + "\"" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return  Objects.equals(isReadByProfilesId, that.isReadByProfilesId) && //todo (Nick Dolgopolov) как использовать isReadByProfilesId?
                Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(creatorProfile, that.creatorProfile) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, creatorProfile, date, isReadByProfilesId); //todo (Nick Dolgopolov) как использовать isReadByProfilesId?
    }

    @Override
    public int compareTo(ChatMessage o) {
        return Long.compare(o.getDate().getTime(), this.getDate().getTime());
    }
}
