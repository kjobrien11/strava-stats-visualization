package com.kjobrien.strava_visualization.POJO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class StavaApi {

    private String url = "https://www.strava.com/api/v3/athlete/activities?after=1735707600&per_page=50";
    private JsonNode jsonResposne;
    private String token;

    public StavaApi() {
        long currentEpochSeconds = Instant.now().getEpochSecond();
        System.out.println("Epoch Seconds: " + currentEpochSeconds);
        refrehToken();
        initializeStats();
    }

    public void refrehToken() {
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + token);
            String urlWithParams = UriComponentsBuilder.fromHttpUrl("https://www.strava.com/oauth/token")
                    .queryParam("client_id", System.getenv("CLIENT_ID"))
                    .queryParam("client_secret", System.getenv("CLIENT_SECRET"))
                    .queryParam("refresh_token", System.getenv("STRAVA_REFRESH_TOKEN"))
                    .queryParam("grant_type", "refresh_token")
                    .toUriString();
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, entity, String.class);
            setToken(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to resfresh token", e);
        }
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

    private void setToken(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonResposne = objectMapper.readTree(response.getBody());
            token = jsonResposne.get("access_token").asText();
            System.out.println(token);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }

    public JsonNode getJsonReponse() {
        return jsonResposne;
    }
}
