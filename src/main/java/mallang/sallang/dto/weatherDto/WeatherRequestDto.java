package mallang.sallang.dto.weatherDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherRequestDto {
    private double lat;
    private double lon;
}