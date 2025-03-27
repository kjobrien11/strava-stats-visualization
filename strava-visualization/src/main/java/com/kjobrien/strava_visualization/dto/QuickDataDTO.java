package com.kjobrien.strava_visualization.dto;

public class QuickDataDTO {
    String title= "Title";
    double value = 0;
    String units = "units";

    public QuickDataDTO(String title, double value, String units) {
        this.title = title;
        this.value = value;
        this.units = units;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
