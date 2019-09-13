package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SeekerShowName extends SeekerProfile implements Serializable {

    public SeekerShowName() {}

    @JsonValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getPatronymic() {
        return super.getPatronymic();
    }

    @Override
    public String getSurname() {
        return super.getSurname();
    }

}
