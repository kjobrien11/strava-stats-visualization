package com.kjobrien.strava_visualization.POJO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StavaApi {

    private String token = System.getenv("API_KEY");
    private String url = "https://www.strava.com/api/v3/athlete/activities?after=1735707600&per_page=50";
    private JsonNode jsonResposne;

    public StavaApi() {
        initializeStats();
    }

    public void initializeStats() {
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            setJsonReponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Strava stats", e);
        }
    }

    private void setJsonReponse(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.jsonResposne = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }

    public JsonNode getJsonReponse() {
        return jsonResposne;
    }
}
