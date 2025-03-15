package com.kjobrien.strava_visualization.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjobrien.strava_visualization.POJO.StavaApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class StravaApiService {

    private final StringHttpMessageConverter stringHttpMessageConverter;
    StavaApi stavaApi = new StavaApi();

    public StravaApiService(StringHttpMessageConverter stringHttpMessageConverter) {
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }

    public String getAthleteStats() {
        return stavaApi.getJsonReponse().toString();
    }

    public int getRunCount(){
        JsonNode rootNode = stavaApi.getJsonReponse();
        int count = 0;
        if (rootNode.isArray()) {
            for (JsonNode activityNode : rootNode) {
                if(activityNode.path("name").asText().endsWith("Run")){
                    count++;
                }
            }
        }
        return count;
    }

    public String refreshStats(){
        stavaApi.initializeStats();
        return getAthleteStats();
    }

    public double getTotalDistance(){
        return stavaApi.getTotalDistance();
    }

    public long getTotalTime()  {
        return stavaApi.getTotalWorkoutTime();
    }

    public int getTotalRuns()  {
        return stavaApi.getTotalRuns();
    }

}
