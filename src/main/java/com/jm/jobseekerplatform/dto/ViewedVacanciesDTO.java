package com.jm.jobseekerplatform.dto;

public class ViewedVacanciesDTO {

    private Long id;
    private String headline;
    private String companyname;
    private int salarymin;
    private int salarymax;

    public ViewedVacanciesDTO() {
    }

    public ViewedVacanciesDTO(Long id, String headline, String companyname, int salarymin, int salarymax) {
        this.id = id;
        this.headline = headline;
        this.companyname = companyname;
        this.salarymin = salarymin;
        this.salarymax = salarymax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public int getSalarymin() {
        return salarymin;
    }

    public void setSalarymin(int salarymin) {
        this.salarymin = salarymin;
    }

    public int getSalarymax() {
        return salarymax;
    }

    public void setSalarymax(int salarymax) {
        this.salarymax = salarymax;
    }
}
