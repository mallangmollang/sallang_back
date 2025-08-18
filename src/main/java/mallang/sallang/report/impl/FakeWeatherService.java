package mallang.sallang.report.impl;

import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.port.WeatherProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//더미 구현 -> 시간대/위치 무시, 고정 규칙
@Component
@Profile("!fake")
public class FakeWeatherService implements WeatherProvider {

    @Override
    public WeatherData fetch(double lat, double lon, LocalDate date) {
        // 간단한 의사 난수성: 위도/경도/날짜로 온습도 생성
        int seed = (int) Math.abs(Math.round(lat * 1000) + Math.round(lon * 1000) + date.toEpochDay());
        double temperature = 26 + (seed % 100) * 0.05; // 26 ~ 31도 근처
        double humidity = 50 + (seed % 50);            // 50 ~ 99%
        return new WeatherData(round1(temperature), (int) round1(humidity));
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
