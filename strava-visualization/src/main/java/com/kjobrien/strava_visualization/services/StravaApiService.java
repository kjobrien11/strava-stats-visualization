package com.kjobrien.strava_visualization.services;

import com.kjobrien.strava_visualization.POJO.StavaApi;
import org.springframework.stereotype.Service;

@Service
public class StravaApiService {

    StavaApi stavaApi = new StavaApi();

    public String getAthleteStats() {
        return stavaApi.getJsonReponse().toString();
    }

    public String refreshStats(){
        stavaApi.requestJson();
        return getAthleteStats();
    }

    public double getTotalDistanceInMeters(){
        return stavaApi.getTotalDistanceInMeters();
    }

    public long getTotalWorkoutTimeInSeconds()  {
        return stavaApi.getTotalWorkoutTimeInSeconds();
    }

    public int getTotalRuns()  {
        return stavaApi.getTotalRuns();
    }

}
