package com.kjobrien.strava_visualization.dto;

public class Workout {

    private double distance;
    private long timeInSeconds;
    private String type;
    private String date;
    private double averageSpeed;
    private double topSpeed;
    private double averageHeartRate;

    public Workout(double distance, long timeInSeconds, String type, String date, double averageSpeed, double topSpeed, double averageHeartRate) {
        this.distance = distance;
        this.timeInSeconds = timeInSeconds;
        this.type = type;
        this.date = date;
        this.averageSpeed = averageSpeed;
        this.topSpeed = topSpeed;
        this.averageHeartRate = averageHeartRate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }

    public double getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(double averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "distance=" + distance +
                ", timeInSeconds=" + timeInSeconds +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", topSpeed=" + topSpeed +
                ", averageHeartRate=" + averageHeartRate +
                '}';
    }
}
