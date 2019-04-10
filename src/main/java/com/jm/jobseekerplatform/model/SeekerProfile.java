package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "seekerprofiles")
public class SeekerProfile implements Serializable {

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
