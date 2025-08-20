package mallang.sallang.dto.weatherDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherResponseDto {
    private int humidityPercentage;
    private int uvIndex;
    private double temperature;
}
