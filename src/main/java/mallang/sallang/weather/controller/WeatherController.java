package mallang.sallang.weather.controller;

import lombok.RequiredArgsConstructor;
import mallang.sallang.weather.service.WeatherInfoService;
import mallang.sallang.weather.dto.WeatherRequestDto;
import mallang.sallang.weather.dto.WeatherResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "https://sunny-boba-b65221.netlify.app")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherInfoService weatherInfoService;

    @PostMapping
    public WeatherResponseDto getWeather(@RequestBody WeatherRequestDto requestDto) {
        JsonNode weatherData = weatherInfoService.getWeatherInfo(requestDto.getLat(), requestDto.getLon());

        double temperature = weatherInfoService.getTemperature(weatherData);
        int humidity = weatherInfoService.getHumidity(weatherData);
        int uvIndex = weatherInfoService.getUvIndex(weatherData);

        return new WeatherResponseDto(humidity, uvIndex, temperature);
    }
}
