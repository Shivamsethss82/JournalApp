
package com.shivam.journalApp.service;

import com.shivam.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    private static final String apiKey = "1af49f03b116e0718bd78ea325c40e31";

    private static final String API =
            "http://api.weatherstack.com/current?access_key={key}&query={city}";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        ResponseEntity<WeatherResponse> response =
                restTemplate.exchange(API, HttpMethod.GET, null, WeatherResponse.class, apiKey, city);
        return response.getBody();
    }
}
