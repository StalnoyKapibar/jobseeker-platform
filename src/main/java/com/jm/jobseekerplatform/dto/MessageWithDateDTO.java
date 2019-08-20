package com.jm.jobseekerplatform.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageWithDateDTO extends MessageDTO implements Comparable<MessageWithDateDTO>{

    private Date date;


    public MessageWithDateDTO(Long creatorProfileId, List<String> members, String text, Date date) {
        super(creatorProfileId, members, text,date);
        this.date = date;
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageWithDateDTO that = (MessageWithDateDTO) o;
        return date.equals(that.date);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    @Override
    public int compareTo(MessageWithDateDTO o) {
        return Long.compare(o.getId(), this.getId());
    }
}
