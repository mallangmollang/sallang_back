package mallang.sallang.report.dto;

import java.time.LocalDate;
import java.util.List;

public class ReportResponseDto {

    public LocalDate date;
    public Location location;
    public Weather weather;
    public DailyRaw raw;
    public HealthResult health;

    public static class Location {
        public double lat;
        public double lon;
    }

    public static class Weather {
        public double temperature;
        public double humidity;
    }

    // 프론트에서 그대로 표시할 수 있는 원시값
    public static class DailyRaw {
        public int steps;
        public int waterIntake;
        public int workMinutes;
        public int restMinutes;
    }

    // AI 섹션(점수, 코멘트, 제안)
    public static class HealthResult {
        public Scores scores;
        public List<String> analysisComments;
        public List<String> tomorrowSuggestions;
    }

    // 건강 점수
    public static class Scores {
        public int hydration;
        public int rest;
        public int symptom;
    }
}
