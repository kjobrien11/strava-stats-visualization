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
import com.kjobrien.strava_visualization.dto.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class StavaApi {

    //Constants used for retrieving API data
    private final long START_EPOCH =1735707600; // January 1st, 2025
    private final int ACTIVITIES_PER_PAGE = 50;
    private final String STRAVA_API_URL = "https://www.strava.com/api/v3/athlete/activities?after=+"+ START_EPOCH +"&per_page="+ ACTIVITIES_PER_PAGE;
    private final String REFRESH_TOKEN;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private String ACCESS_TOKEN;
    //    private long expirationEpoch;

    //Raw Data
    private final List<Run> runs = new ArrayList<>();
    private JsonNode unformattedRuns;

    //Chart Data
    List<WeekActivityDTO> lineChartData = new ArrayList<>();
    List<WeekActivityDTO> barChartData = new ArrayList<>();

    //Quick Data
    private int totalRuns = 0;
    private long totalTimeInSeconds = 0;
    private double totalDistanceInMiles = 0;
    private double longestRunDistanceInMiles = 0;
    private double cumulativeAverageHeartRate = 0;
    private double cumulativeAverageSpeed = 0;



    //creates StravaApi and injects API values
    @Autowired
    public StavaApi(@Value("${api.refresh_token}") String refreshToken, @Value("${api.client_id}") String clientId, @Value("${api.client_secret}") String clientSecret) {
        this.REFRESH_TOKEN = refreshToken;
        this.CLIENT_ID = clientId;
        this.CLIENT_SECRET = clientSecret;
        long currentEpochSeconds = Instant.now().getEpochSecond();
        System.out.println("Epoch Seconds: " + currentEpochSeconds);
        refreshAccessToken();
        requestWorkoutsFromStrava();
        formatRunsAndGenerateQuickData();
        generateChartData();
    }

    //Queries external Stava API to get the valid access token
    public void refreshAccessToken() {
        System.out.println("Refreshing Token");
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String urlWithParams = UriComponentsBuilder.fromHttpUrl("https://www.strava.com/oauth/token")
                    .queryParam("client_id", CLIENT_ID)
                    .queryParam("client_secret", CLIENT_SECRET)
                    .queryParam("refresh_token", REFRESH_TOKEN)
                    .queryParam("grant_type", "refresh_token")
                    .toUriString();
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, entity, String.class);
            setAccessToken(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token", e);
        }
    }

    //Sets the Strava Access Token
    private void setAccessToken(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            ACCESS_TOKEN = responseJson.get("access_token").asText();
//            expirationEpoch =  responseJson.get("expires_at").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not set token", e);
        }
    }

    public void requestWorkoutsFromStrava() {
        try {
            String jsonBody = "{\"key\":\"value\"}";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(STRAVA_API_URL, HttpMethod.GET, entity, String.class);
            setUnformattedRuns(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Strava stats", e);
        }
    }

    private void setUnformattedRuns(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            unformattedRuns = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse unformatted workouts", e);
        }
    }

    private void formatRunsAndGenerateQuickData(){
        int workoutCount = 0;
        long seconds = 0;
        double distance = 0;
        double totalHeartRateActivities = 0;

        if (unformattedRuns.isArray()) {
            for (JsonNode activityNode : unformattedRuns) {
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
                    cumulativeAverageSpeed += averageSpeed;
                    double topSpeed = activityNode.path("max_speed").asDouble();
                    double averageHeartRate = 0;
                    if(activityNode.path("has_heartrate").asBoolean()){
                        averageHeartRate = activityNode.path("average_heartrate").asDouble();
                        cumulativeAverageHeartRate += averageHeartRate;
                        totalHeartRateActivities++;
                    }

                    Run current = new Run(distanceRan, timeInSeconds, type, LocalDate.parse(date.substring(0, 10)), averageSpeed, topSpeed, averageHeartRate);
                    runs.add(current);
                }
            }
            totalRuns = workoutCount;
            totalTimeInSeconds = seconds;
            totalDistanceInMiles = distance;
            cumulativeAverageHeartRate = cumulativeAverageHeartRate /totalHeartRateActivities;
            cumulativeAverageSpeed = cumulativeAverageSpeed /totalRuns*2.237;

        }
    }

    public void generateChartData(){
        double distanceWeek = 0;
        double distanceTotal = 0;
        LocalDate startDate = LocalDate.of(2024, 12, 30);
        LocalDate endOfWeekDay = LocalDate.of(2025, 1, 6);
        lineChartData.add(new WeekActivityDTO(0, startDate));

        for(int i = 0; i < runs.size(); i++){
            if(runs.get(i).getDate().isBefore(endOfWeekDay)){
                distanceWeek+= runs.get(i).getDistance();
                distanceTotal+= runs.get(i).getDistance();
            }else{
                barChartData.add(new WeekActivityDTO(distanceWeek, startDate.plusWeeks(1)));
                lineChartData.add(new WeekActivityDTO(distanceTotal, startDate.plusWeeks(1)));
                distanceWeek = runs.get(i).getDistance();
                distanceTotal += runs.get(i).getDistance();
                startDate = startDate.plusWeeks(1);
                endOfWeekDay= endOfWeekDay.plusWeeks(1);

            }
        }
        barChartData.add(new WeekActivityDTO(distanceWeek, startDate.plusWeeks(1)));
        lineChartData.add(new WeekActivityDTO(distanceTotal, startDate.plusWeeks(1)));
    }

    public QuickDataDTO createQuickDataItem(String title, double value, String units){
        return new QuickDataDTO(title, value, units);
    }

    public double getTotalDistanceInMiles(){
        return totalDistanceInMiles;
    }

    public long getTotalWorkoutTimeInSeconds(){
        return totalTimeInSeconds;
    }

    public int getTotalRuns(){
        return totalRuns;
    }

    public double getLongestRunDistanceInMiles() {
        return longestRunDistanceInMiles;
    }

    public double getCumulativeAverageSpeed() {
        return cumulativeAverageSpeed;
    }

    public double getCumulativeAverageHeartRate() {
        return cumulativeAverageHeartRate;
    }

    public List<Run> getRuns() {
        return runs;
    }

    public List<WeekActivityDTO> getBarChartData() {
        return barChartData;
    }

    public List<WeekActivityDTO> getLineChartData() {
        return lineChartData;
    }

}
