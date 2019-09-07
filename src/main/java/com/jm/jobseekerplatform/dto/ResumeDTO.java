package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

public class ResumeDTO {
	Long id;
	String name;
	String patronymic;
	String surname;

	ResumeDTO() {}

	ResumeDTO(SeekerProfile seekerProfile) {
		this.id = seekerProfile.getId();
		this.name = seekerProfile.getName();
		this.patronymic = seekerProfile.getPatronymic();
		this.surname = seekerProfile.getSurname();
	}

	ResumeDTO(Long id, String name, String patronymic, String surname) {
		this.id = id;
		this.name = name;
		this.patronymic = patronymic;
		this.surname = surname;
	}




}
