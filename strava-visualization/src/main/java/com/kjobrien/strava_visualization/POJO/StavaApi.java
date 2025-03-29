package com.kjobrien.strava_visualization.POJO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kjobrien.strava_visualization.dto.QuickDataDTO;
import com.kjobrien.strava_visualization.dto.WeekActivityDTO;
import com.kjobrien.strava_visualization.dto.Workout;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class StavaApi {

    private long startEpoch =1735707600;
    private int activitiesPerPage = 50;
    private String activities_url = "https://www.strava.com/api/v3/athlete/activities?after=1735707600&per_page=50";
    private JsonNode jsonResposne;
    private String token;
    private long expirationEpoch;
    private int totalRuns;
    private long workoutTimeInSeconds;
    private double totalDistanceInMiles;
    private double longestRunDistanceInMiles = 0;
    private double sumAverageHeartRate = 0;
    private double sumAverageSpeed = 0;
    private List<Workout> workouts = new ArrayList<Workout>();
    List<WeekActivityDTO> weeklyDistance = new ArrayList<WeekActivityDTO>();
    List<WeekActivityDTO> cumulativeDistance = new ArrayList<WeekActivityDTO>();

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public StavaApi() {
        setUpApi();
    }

    public StavaApi(long startEpoch, int activitiesPerPage) {
        this.startEpoch = startEpoch;
        this.activitiesPerPage = activitiesPerPage;
        this.activities_url = "https://www.strava.com/api/v3/athlete/activities?after=" + startEpoch +"&per_page="+activitiesPerPage;
        setUpApi();
    }

    private void setUpApi(){
        long currentEpochSeconds = Instant.now().getEpochSecond();
        System.out.println("Epoch Seconds: " + currentEpochSeconds);
        refrehToken();
        requestJson();
        generateStats();
        generateWeekActivityTotals();
    }

    public void refrehToken() {
        System.out.println("Refreshing Token");
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String urlWithParams = UriComponentsBuilder.fromHttpUrl("https://www.strava.com/oauth/token")
                    .queryParam("client_id", System.getenv("CLIENT_ID"))
                    .queryParam("client_secret", System.getenv("CLIENT_SECRET"))
                    .queryParam("refresh_token", System.getenv("REFRESH_TOKEN"))
                    .queryParam("grant_type", "refresh_token")
                    .toUriString();
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, entity, String.class);
            setToken(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to resfresh token", e);
        }
    }

    public void requestJson() {
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(activities_url, HttpMethod.GET, entity, String.class);
            setJsonReponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Strava stats", e);
        }
    }

    private void setJsonReponse(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonResposne = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }

    private void setToken(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            token = responseJson.get("access_token").asText();
            expirationEpoch =  responseJson.get("expires_at").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not set token", e);
        }
    }

    public JsonNode getJsonReponse() {
        return jsonResposne;
    }

    private void generateStats(){
        int workoutCount = 0;
        long seconds = 0;
        double distance = 0;
        double totalHeartRateActivities = 0;

        if (jsonResposne.isArray()) {
            for (JsonNode activityNode : jsonResposne) {
                if(activityNode.path("type").asText().equals("Run")){
                    workoutCount++;
                    seconds += activityNode.path("moving_time").asLong();
                    distance += activityNode.path("distance").asDouble()/1609;

                    double distanceRan = activityNode.path("distance").asDouble() /1609;
                    if(distanceRan > longestRunDistanceInMiles){
                        longestRunDistanceInMiles = distanceRan;
                    }
                    long timeInSeconds = activityNode.path("moving_time").asLong();
                    String type = activityNode.path("type").asText();
                    String date = activityNode.path("start_date").asText();
                    double averageSpeed = activityNode.path("average_speed").asDouble();
                    sumAverageSpeed += averageSpeed;
                    double topSpeed = activityNode.path("max_speed").asDouble();
                    double averageHeartRate = 0;
                    if(activityNode.path("has_heartrate").asBoolean()){
                        averageHeartRate = activityNode.path("average_heartrate").asDouble();
                        sumAverageHeartRate += averageHeartRate;
                        totalHeartRateActivities++;
                    }

                    Workout current = new Workout(distanceRan, timeInSeconds, type, LocalDate.parse(date.substring(0, 10)), averageSpeed, topSpeed, averageHeartRate);
                    workouts.add(current);
                }
            }
            totalRuns = workoutCount;
            workoutTimeInSeconds = seconds;
            totalDistanceInMiles = distance;
            sumAverageHeartRate = sumAverageHeartRate/totalHeartRateActivities;
            sumAverageSpeed = sumAverageSpeed/totalRuns;

        }
    }

    public void generateWeekActivityTotals(){
        double distanceWeek = 0;
        double distanceTotal = 0;
        LocalDate startDate = LocalDate.of(2024, 12, 30);
        LocalDate endOfWeekDay = LocalDate.of(2025, 1, 6);
        cumulativeDistance.add(new WeekActivityDTO(0, startDate));

        for(int i = 0; i < workouts.size(); i++){
            if(workouts.get(i).getDate().isBefore(endOfWeekDay)){
                distanceWeek+=workouts.get(i).getDistance();
                distanceTotal+=workouts.get(i).getDistance();
            }else{
                weeklyDistance.add(new WeekActivityDTO(distanceWeek, startDate.plusWeeks(1)));
                cumulativeDistance.add(new WeekActivityDTO(distanceTotal, startDate.plusWeeks(1)));
                distanceWeek = workouts.get(i).getDistance();
                distanceTotal +=workouts.get(i).getDistance();
                startDate = startDate.plusWeeks(1);
                endOfWeekDay= endOfWeekDay.plusWeeks(1);

            }
        }
        weeklyDistance.add(new WeekActivityDTO(distanceWeek, startDate.plusWeeks(1)));
        cumulativeDistance.add(new WeekActivityDTO(distanceTotal, startDate.plusWeeks(1)));

    }

    public QuickDataDTO createQuickDataItem(String title, double value, String units){
        return new QuickDataDTO(title, value, units);
    }

    public double getTotalDistanceInMiles(){
        return totalDistanceInMiles;
    }

    public long getTotalWorkoutTimeInSeconds(){
        return workoutTimeInSeconds;
    }

    public int getTotalRuns(){
        return totalRuns;
    }

    public double getLongestRunDistanceInMiles() {
        return longestRunDistanceInMiles;
    }

    public double getSumAverageSpeed() {
        return sumAverageSpeed;
    }

    public double getSumAverageHeartRate() {
        return sumAverageHeartRate;
    }

    public List<WeekActivityDTO> getWeeklyDistance() {
        return weeklyDistance;
    }

    public List<WeekActivityDTO> getCumulativeDistance() {
        return cumulativeDistance;
    }


}
