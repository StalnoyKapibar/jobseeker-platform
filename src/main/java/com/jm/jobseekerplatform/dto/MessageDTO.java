package com.jm.jobseekerplatform.dto;

import java.util.Date;
import java.util.Objects;

public class MessageDTO implements Comparable<MessageDTO>{

    private Long vacancyId;

    private String vacancyHeadline;

    private String lastMessage;

    private Date date;

    private String adminTo;

    public MessageDTO(Long vacancyId, String vacancyHeadline, String lastMessage, Date date, String adminTo) {
        this.vacancyId = vacancyId;
        this.vacancyHeadline = vacancyHeadline;
        this.lastMessage = lastMessage;
        this.date = date;
        this.adminTo = adminTo;

    }

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

    public String getAdminTo() {
        return adminTo;
    }

    public void setAdminTo(String adminTo) {
        this.adminTo = adminTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(vacancyId, that.vacancyId) &&
                Objects.equals(vacancyHeadline, that.vacancyHeadline) &&
                Objects.equals(lastMessage, that.lastMessage) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacancyId, vacancyHeadline, lastMessage, date);
    }

    @Override
    public int compareTo(MessageDTO o) {
        return Long.compare(o.getDate().getTime(), this.getDate().getTime());
    }
}
