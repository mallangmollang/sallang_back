package mallang.sallang.report.port;

import mallang.sallang.report.domain.WeatherData;

import java.time.LocalDate;

public interface WeatherProvider {
    WeatherData fetch(double lat, double lon, LocalDate date);
}
