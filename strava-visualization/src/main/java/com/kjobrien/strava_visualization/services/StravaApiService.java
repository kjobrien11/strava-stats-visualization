package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
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

    public double getTotalDistanceInMiles()  {
        return stravaApi.getTotalDistanceInMiles();
    }

    public long getTotalWorkoutTimeInSeconds()  {
        return stravaApi.getTotalWorkoutTimeInSeconds();
    }

    public int getTotalRuns()  {
        return stravaApi.getTotalRuns();
    }

    public List<WeekActivityDTO> getCumulativeDistance(){
        return stravaApi.getCumulativeDistance();

    }

    public List<WeekActivityDTO> getWeeklyDistance(){
        return stravaApi.getWeeklyDistance();
    }

}
