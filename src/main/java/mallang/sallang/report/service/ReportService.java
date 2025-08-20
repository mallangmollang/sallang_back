package mallang.sallang.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.dto.ReportResponseDto;
import mallang.sallang.report.port.HealthAdvisor;
import mallang.sallang.weather.WeatherInfoService;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final WeatherInfoService weather;
    private final HealthAdvisor advisor;
    private final ReportResponseMappers mappers;

    public ReportService(WeatherInfoService weather, HealthAdvisor advisor, ReportResponseMappers mappers) {
        this.weather = weather;
        this.advisor = advisor;
        this.mappers = mappers;
    }

    public ReportResponseDto createDailyReport(ReportRequestDto req) {
        JsonNode weatherInfoJson = weather.getWeatherInfo(req.getLat(), req.getLon());
        double temp = weather.getTemperature(weatherInfoJson);
        int humidity = weather.getHumidity(weatherInfoJson);
        WeatherData w = new WeatherData(temp, humidity);
        AiAdvice a = advisor.analyze(req, w);
        return mappers.toResponse(req, w, a);
    }
}
