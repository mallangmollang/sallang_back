package mallang.sallang.report.port;

import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;

public interface HealthAdvisor {
    AiAdvice analyze(ReportRequestDto req, WeatherData weather);
}
