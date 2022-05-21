package com.ensure.vac;

class GetLocationModel {
    Double latitude;
    Double longitude;
    String teamName;

    public GetLocationModel() {

    }

    public GetLocationModel(Double latitude, Double longitude, String teamName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.teamName = teamName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTeamName() {
        return teamName;
    }
}
