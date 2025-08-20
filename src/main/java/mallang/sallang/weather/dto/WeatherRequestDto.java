package mallang.sallang.weather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherRequestDto {
    private double lat;
    private double lon;
}