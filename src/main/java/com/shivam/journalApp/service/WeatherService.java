package com.shivam.journalApp.service;

import com.shivam.journalApp.api.response.WeatherResponse;
import com.shivam.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city) {
        String api = appCache.APPCACHE.get("weather_api");
        if (api == null) {
            return null;   // weather_api entry missing in database
        }

        ResponseEntity<WeatherResponse> response =
                restTemplate.exchange(api, HttpMethod.GET, null, WeatherResponse.class, apiKey, city);
        return response.getBody();
    }
}