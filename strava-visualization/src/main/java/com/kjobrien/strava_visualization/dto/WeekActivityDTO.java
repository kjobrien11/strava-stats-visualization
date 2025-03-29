package com.kjobrien.strava_visualization.dto;

import java.time.LocalDate;

public class WeekActivityDTO {
    double weekDistance = 0;
    LocalDate startDate = LocalDate.now();

    public WeekActivityDTO() {}

    public WeekActivityDTO(double weekDistance, LocalDate startDate) {
        this.weekDistance = weekDistance;
        this.startDate = startDate;
    }

    public double getWeekDistance() {
        return weekDistance;
    }

    public void setWeekDistance(double weekDistance) {
        this.weekDistance = weekDistance;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "WeekActivityDTO{" +
                "weekDistance=" + weekDistance +
                ", startDate=" + startDate +
                '}';
    }
}
