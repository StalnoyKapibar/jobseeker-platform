package com.jm.jobseekerplatform.model.profiles;

import javax.persistence.Entity;

@Entity
public class TestProfile extends Profile{

	public TestProfile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getFullName() {
		// TODO Auto-generated method stub
		return "TestProfile";
	}

	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return "Test";
	}

}
