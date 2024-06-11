package com.job.sagar.model;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name = "jobs")
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobid;
    private int eid;
    private String title;
    private String jobdesc;
    private int vacno;
    private String experience;
    private String basicpay;
    private String fnarea;
    private String location;
    private String industry;
    private String ugqual;
    private String pgqual;
    private String profile;
    private String postdate;

    // Constructors, getters, and setters

    public Jobs() {
    }

    public Jobs(int eid, String title, String jobdesc, int vacno, String experience, String basicpay, String fnarea,
            String location, String industry, String ugqual, String pgqual, String profile, String postdate) {
        this.eid = eid;
        this.title = title;
        this.jobdesc = jobdesc;
        this.vacno = vacno;
        this.experience = experience;
        this.basicpay = basicpay;
        this.fnarea = fnarea;
        this.location = location;
        this.industry = industry;
        this.ugqual = ugqual;
        this.pgqual = pgqual;
        this.profile = profile;
        this.postdate = postdate;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public int getVacno() {
        return vacno;
    }

    public void setVacno(int vacno) {
        this.vacno = vacno;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBasicpay() {
        return basicpay;
    }

    public void setBasicpay(String basicpay) {
        this.basicpay = basicpay;
    }

    public String getFnarea() {
        return fnarea;
    }

    public void setFnarea(String fnarea) {
        this.fnarea = fnarea;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getUgqual() {
        return ugqual;
    }

    public void setUgqual(String ugqual) {
        this.ugqual = ugqual;
    }

    public String getPgqual() {
        return pgqual;
    }

    public void setPgqual(String pgqual) {
        this.pgqual = pgqual;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
}