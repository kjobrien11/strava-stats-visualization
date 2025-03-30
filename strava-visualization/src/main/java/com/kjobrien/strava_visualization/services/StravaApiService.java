package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
import com.kjobrien.strava_visualization.dto.QuickDataDTO;
import com.kjobrien.strava_visualization.dto.WeekActivityDTO;
import com.kjobrien.strava_visualization.dto.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StravaApiService {

    //January 1, 2025 & 50 activities
    @Autowired
    private StavaApi stravaApi;

    public List<Run> getAthleteStats() {
        return stravaApi.getRuns();
    }

    public List<Run> refreshStats(){
        stravaApi.refreshAccessToken();
        stravaApi.requestWorkoutsFromStrava();
        return getAthleteStats();
    }

    public double getTotalDistanceInMeters(){
        return stravaApi.getTotalDistanceInMiles()*1609.0;
    }

    public QuickDataDTO getTotalDistanceInMiles()  {
        return stravaApi.createQuickDataItem("Total Distance Ran",  stravaApi.getTotalDistanceInMiles(), "Miles");
    }

    public QuickDataDTO getTotalWorkoutTimeInSeconds()  {
        return stravaApi.createQuickDataItem("Total Hours Ran",  ((double)stravaApi.getTotalWorkoutTimeInSeconds())/3600, "Hours");
    }

    public QuickDataDTO getTotalRuns()  {
        return stravaApi.createQuickDataItem("Total Runs", stravaApi.getTotalRuns(), "Runs");
    }

    public QuickDataDTO getLongestRunDistanceInMiles()  {
        return stravaApi.createQuickDataItem("Longest Run", stravaApi.getLongestRunDistanceInMiles(), "Miles");
    }

    public QuickDataDTO getAverageSpeed()  {
        return stravaApi.createQuickDataItem("Average Speed", stravaApi.getCumulativeAverageSpeed(), "MPH");
    }

    public QuickDataDTO getAverageHeartRate()  {
        return stravaApi.createQuickDataItem("Average Heart Rate", stravaApi.getCumulativeAverageHeartRate(), "BPM");
    }

    public List<WeekActivityDTO> getCumulativeDistance(){
        return stravaApi.getLineChartData();
    }

    public List<WeekActivityDTO> getWeeklyDistance(){
        return stravaApi.getBarChartData();
    }
}
