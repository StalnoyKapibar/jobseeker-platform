package com.jm.jobseekerplatform.model.createdByProfile;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CreatedBySeekerProfileBase extends CreatedByProfileBase<SeekerProfile> {
    public CreatedBySeekerProfileBase() {
    }

    public CreatedBySeekerProfileBase(SeekerProfile creatorProfile) {
        super(creatorProfile);
    }

    @Override
    public String toString() {
        return "CreatedBySeekerProfileBase{" +
                super.toString() +
                '}';
    }
}
