package mallang.sallang.report.service;

import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.dto.ReportResponseDto;
import mallang.sallang.report.port.HealthAdvisor;
import mallang.sallang.report.port.WeatherProvider;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final WeatherProvider weather;
    private final HealthAdvisor advisor;
    private final ReportResponseMappers mappers;

    public ReportService(WeatherProvider weather, HealthAdvisor advisor, ReportResponseMappers mappers) {
        this.weather = weather;
        this.advisor = advisor;
        this.mappers = mappers;
    }

    public ReportResponseDto createDailyReport(ReportRequestDto req) {
        WeatherData w = weather.fetch(req.lat, req.lon, req.date);
        AiAdvice a = advisor.analyze(req, w);
        return mappers.toResponse(req, w, a);
    }
}
