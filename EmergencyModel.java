package com.ensure.vac;

public class EmergencyModel {
    String JobID;
    String issue;
    String teamName;
    String workers;
    String area;

    public EmergencyModel() {

    }

    public EmergencyModel(String JobID, String issue, String teamName, String workers, String area) {
        this.teamName = teamName;
        this.workers = workers;
        this.JobID = JobID;
        this.issue = issue;
        this.area = area;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getWorkers() {
        return workers;
    }

    public String getIssue() {
        return issue;
    }

    public String getUserId() {
        return JobID;
    }

    public String getArea() {
        return area;
    }
}
