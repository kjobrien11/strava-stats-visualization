package com.kjobrien.strava_visualization.dto;

public class QuickDataDTO {
    String title= "Title";
    double value = 0;
    String units = "units";

    public QuickDataDTO(String units, double value, String title) {
        this.units = units;
        this.value = value;
        this.title = title;
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
