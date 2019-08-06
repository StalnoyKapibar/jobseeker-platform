package com.jm.jobseekerplatform.model.createdByProfile;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CreatedByEmployerProfileBase extends CreatedByProfileBase<EmployerProfile> {
    public CreatedByEmployerProfileBase() {
    }

    public CreatedByEmployerProfileBase(EmployerProfile creatorProfile) {
        super(creatorProfile);
    }

    @Override
    public String toString() {
        return "CreatedByEmployerProfileBase{" +
                super.toString() +
                '}';
    }
}
