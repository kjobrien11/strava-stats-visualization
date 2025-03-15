package com.kjobrien.strava_visualization.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StravaApiController {

    @GetMapping
    String getAthleteStats() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String token = System.getenv("API_KEY");

        //Return 50 workouts after january 1st
        String url = "https://www.strava.com/api/v3/athlete/activities?after=1735707600&per_page=50";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        String jsonBody = "{\"key\":\"value\"}";
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        int count = 0;
        if (rootNode.isArray()) {
            for (JsonNode activityNode : rootNode) {
                if(activityNode.path("name").asText().endsWith("Run")){
                    count++;
                }
            }
        }
        System.out.println("Run Count: " + count);
        return response.getBody();
    }



}