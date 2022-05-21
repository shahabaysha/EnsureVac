package com.ensure.vac;

public class AddWorkerModel {
    String dateOfBirth;
    String cnic;
    String JobID;
    String fullName;
    Boolean isSelected;

    public AddWorkerModel() {

    }

    public AddWorkerModel(String dateOfBirth, String fullName, String JobID, String cnic, Boolean isSelected) {
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.JobID = JobID;
        this.cnic = cnic;
        this.isSelected = isSelected;
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

    public Boolean getIsSelected() {
        return isSelected;
    }
}
