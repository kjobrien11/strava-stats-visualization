package com.kjobrien.strava_visualization.controllers;

import com.kjobrien.strava_visualization.dto.QuickDataDTO;
import com.kjobrien.strava_visualization.dto.WeekActivityDTO;
import com.kjobrien.strava_visualization.dto.Workout;
import com.kjobrien.strava_visualization.services.StravaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class StravaApiController {

    @Autowired
    private StravaApiService stravaApiService;

    @GetMapping("/stats")
    public List<Workout> getAthleteStats() {
        return stravaApiService.getAthleteStats();
    }

    @GetMapping("/refresh")
    public List<Workout> refreshStats()  {
        return stravaApiService.refreshStats();
    }

    @GetMapping("/distance-meters")
    public double getTotalDistanceInMeters()  {
        return stravaApiService.getTotalDistanceInMeters();
    }

    @GetMapping("/distance-miles")
    public QuickDataDTO getTotalDistanceInMiles()  {
        return stravaApiService.getTotalDistanceInMiles();
    }

    @GetMapping("/time")
    public QuickDataDTO getTotalWorkoutTimeInSeconds()  {
        return stravaApiService.getTotalWorkoutTimeInSeconds();
    }

    @GetMapping("/run-count")
    public QuickDataDTO getTotalRuns()  {
        return stravaApiService.getTotalRuns();
    }

    @GetMapping("/cumulative-totals")
    public List<WeekActivityDTO> getCumulativeDistance(){
        return stravaApiService.getCumulativeDistance();
    }

    @GetMapping("/weekly-totals")
    public List<WeekActivityDTO> getWeeklyDistance(){
        return stravaApiService.getWeeklyDistance();
    }

    @GetMapping("/longest-run")
    public QuickDataDTO getLongestRunDistanceInMiles()  {
        return stravaApiService.getLongestRunDistanceInMiles();
    }

    @GetMapping("/average-speed")
    public QuickDataDTO getAverageSpeed()  {
        return stravaApiService.getAverageSpeed();
    }

    @GetMapping("/average-heartrate")
    public QuickDataDTO getAverageHeartRate()  {
        return stravaApiService.getAverageHeartRate();
    }



}