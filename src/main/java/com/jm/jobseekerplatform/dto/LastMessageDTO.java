package com.jm.jobseekerplatform.dto;

import java.util.Date;
import java.util.Objects;

public class LastMessageDTO implements Comparable<LastMessageDTO>{

    private Long id;

    private Long vacancyId;

    private String vacancyHeadline;

    private String lastMessage;

    private Date date;

    private boolean isRead;

    private String author;

    public LastMessageDTO() {};

    public LastMessageDTO(Long id, Long vacancyId, String vacancyHeadline, String lastMessage, Date date, boolean isRead, String author) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.vacancyHeadline = vacancyHeadline;
        this.lastMessage = lastMessage;
        this.date = date;
        this.isRead = isRead;
        this.author = author;

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getVacancyId() {
        return vacancyId;
    }

    public String getVacancyHeadline() {
        return vacancyHeadline;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setVacancyId(Long vacancyId) {
        this.vacancyId = vacancyId;
    }

    public void setVacancyHeadline(String vacancyHeadline) {
        this.vacancyHeadline = vacancyHeadline;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastMessageDTO that = (LastMessageDTO) o;
        return isRead == that.isRead &&
                Objects.equals(id, that.id) &&
                Objects.equals(vacancyId, that.vacancyId) &&
                Objects.equals(vacancyHeadline, that.vacancyHeadline) &&
                Objects.equals(lastMessage, that.lastMessage) &&
                Objects.equals(date, that.date) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vacancyId, vacancyHeadline, lastMessage, date, isRead, author);
    }

    @Override
    public int compareTo(LastMessageDTO o) {
        return Long.compare(o.getId(), this.getId());
    }
}
