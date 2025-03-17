package com.kjobrien.strava_visualization.controllers;

import com.kjobrien.strava_visualization.dto.Workout;
import com.kjobrien.strava_visualization.services.StravaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
    public double getTotalDistanceInMiles()  {
        return stravaApiService.getTotalDistanceInMiles();
    }

    @GetMapping("/time")
    public long getTotalWorkoutTimeInSeconds()  {
        return stravaApiService.getTotalWorkoutTimeInSeconds();
    }

    @GetMapping("/run-count")
    public int getTotalRuns()  {
        return stravaApiService.getTotalRuns();
    }



}