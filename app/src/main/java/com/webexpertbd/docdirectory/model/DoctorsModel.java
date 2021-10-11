package com.webexpertbd.docdirectory.model;

public class DoctorsModel {

    String name, department, hospital, number, degree, experience, picture, key;

    public DoctorsModel(String name, String department, String hospital, String number, String degree, String experience, String picture, String key) {
        this.name = name;
        this.department = department;
        this.hospital = hospital;
        this.number = number;
        this.degree = degree;
        this.experience = experience;
        this.picture = picture;
        this.key = key;
    }

    public DoctorsModel() {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
