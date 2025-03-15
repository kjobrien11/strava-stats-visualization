package com.kjobrien.strava_visualization.controllers;

import com.kjobrien.strava_visualization.services.StravaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StravaApiController {

    @Autowired
    private StravaApiService stravaApiService;

    @GetMapping
    public String getAthleteStats() {
        return stravaApiService.getAthleteStats();
    }

    @GetMapping("/refresh")
    public String refreshStats()  {
        return stravaApiService.refreshStats();
    }

    @GetMapping("/distance")
    public double getTotalDistanceInMeters()  {
        return stravaApiService.getTotalDistanceInMeters();
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