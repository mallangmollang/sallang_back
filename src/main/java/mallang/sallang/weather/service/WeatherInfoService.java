package mallang.sallang.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class WeatherInfoService {

    private final RestTemplate restTemplate;

    public JsonNode getWeatherInfo(double latitude, double longitude) {
        String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,uv_index", latitude, longitude);
        return restTemplate.getForObject(url, JsonNode.class);
    }

    public double getTemperature(JsonNode weatherData) {
        return weatherData.path("current").path("temperature_2m").asDouble();
    }

    public int getHumidity(JsonNode weatherData) {
        return weatherData.path("current").path("relative_humidity_2m").asInt();
    }

    public int getUvIndex(JsonNode weatherData) {
        return weatherData.path("current").path("uv_index").asInt();
    }
}