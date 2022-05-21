package com.ensure.vac;

public class JobDetailModel {
    String teamName;
    String jobDescription;
    String date;
    String workers;
    String area;

    public JobDetailModel() {

    }

    public JobDetailModel(String teamName, String jobDescription, String date, String workers, String area) {
        this.teamName = teamName;
        this.jobDescription = jobDescription;
        this.date = date;
        this.workers = workers;
        this.area = area;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getDate() {
        return date;
    }

    public String getWorkers() {
        return workers;
    }

    public String getArea() {
        return area;
    }
}