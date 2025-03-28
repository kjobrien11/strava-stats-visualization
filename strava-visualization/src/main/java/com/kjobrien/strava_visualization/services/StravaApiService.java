package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
import com.kjobrien.strava_visualization.dto.QuickDataDTO;
import com.kjobrien.strava_visualization.dto.WeekActivityDTO;
import com.kjobrien.strava_visualization.dto.Workout;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StravaApiService {

    //January 1, 2025 & 50 activities
    StavaApi stravaApi = new StavaApi(1735707600, 50);

    public List<Workout> getAthleteStats() {
        return stravaApi.getWorkouts();
    }

    public List<Workout> refreshStats(){
        stravaApi.refrehToken();
        stravaApi.requestJson();
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

    public List<WeekActivityDTO> getCumulativeDistance(){
        return stravaApi.getCumulativeDistance();

    }

    public List<WeekActivityDTO> getWeeklyDistance(){
        return stravaApi.getWeeklyDistance();
    }

}
