package com.ensure.vac;

public class AdminModel {
    String JobID;
    String cnic;
    String dateOfBirth;
    String fullName;

    public AdminModel() {

    }

    public AdminModel(String JobID, String cnic, String dateOfBirth, String fullName) {
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.JobID = JobID;
        this.cnic = cnic;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return cnic;
    }

    public String getUserId() {
        return JobID;
    }
}