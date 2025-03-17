package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
import com.kjobrien.strava_visualization.dto.Workout;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StravaApiService {

    StavaApi stavaApi = new StavaApi();

    public List<Workout> getAthleteStats() {
        return stavaApi.getWorkouts();
    }

    public List<Workout> refreshStats(){
        stavaApi.requestJson();
        return getAthleteStats();
    }

    public double getTotalDistanceInMeters(){
        return stavaApi.getTotalDistanceInMeters();
    }

    public double getTotalDistanceInMiles()  {
        return stavaApi.getTotalDistanceInMeters()/1609.0;
    }

    public long getTotalWorkoutTimeInSeconds()  {
        return stavaApi.getTotalWorkoutTimeInSeconds();
    }

    public int getTotalRuns()  {
        return stavaApi.getTotalRuns();
    }

}
