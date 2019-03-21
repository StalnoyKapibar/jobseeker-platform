package com.jm.jobseekerplatform.model;

import javax.persistence.*;

@Entity
@Table(name = "userprofiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "middlename", nullable = false)
    private String middleName;

    @Column(name = "surname", nullable = false)
    private String surname;

}
