package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
import com.kjobrien.strava_visualization.dto.Workout;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StravaApiService {

    StavaApi stravaApi = new StavaApi();

    public List<Workout> getAthleteStats() {
        return stravaApi.getWorkouts();
    }

    public List<Workout> refreshStats(){
        stravaApi.refrehToken();
        stravaApi.requestJson();
        return getAthleteStats();
    }

    public double getTotalDistanceInMeters(){
        return stravaApi.getTotalDistanceInMeters();
    }

    public double getTotalDistanceInMiles()  {
        return stravaApi.getTotalDistanceInMeters()/1609.0;
    }

    public long getTotalWorkoutTimeInSeconds()  {
        return stravaApi.getTotalWorkoutTimeInSeconds();
    }

    public int getTotalRuns()  {
        return stravaApi.getTotalRuns();
    }

}
