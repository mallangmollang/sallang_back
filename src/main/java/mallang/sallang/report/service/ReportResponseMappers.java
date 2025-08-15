package mallang.sallang.report.service;

import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.dto.ReportResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReportResponseMappers {
    public ReportResponseDto toResponse(ReportRequestDto req, WeatherData w, AiAdvice a) {
        ReportResponseDto dto = new ReportResponseDto();

        dto.date = req.date;

        dto.location = new ReportResponseDto.Location();
        dto.location.lat = req.lat;
        dto.location.lon = req.lon;

        dto.weather = new ReportResponseDto.Weather();
        dto.weather.temperatureC = w.temperature();
        dto.weather.humidityPct = w.humidity();

        dto.raw = new ReportResponseDto.DailyRaw();
        dto.raw.steps = req.steps;
        dto.raw.waterIntakeMl = req.waterIntake;
        dto.raw.workMinutes = req.workMinutes;
        dto.raw.restMinutes = req.restMinutes;

        dto.health = new ReportResponseDto.HealthResult();
        dto.health.scores = new ReportResponseDto.Scores();
        dto.health.scores.hydration = a.scores().hydration();
        dto.health.scores.rest = a.scores().rest();
        dto.health.scores.symptom = a.scores().symptom();
        dto.health.analysisComments = a.analysisComments();
        dto.health.tomorrowSuggestions = a.tomorrowSuggestions();

        return dto;
    }
}
