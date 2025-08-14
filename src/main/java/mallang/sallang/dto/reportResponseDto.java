package mallang.sallang.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class reportResponseDto {

    private String message; //종료 메시지
    private Weather weather;

    private int totalSteps;
    private int totalWorkMinutes;
    private int totalRestMinutes;

    private AiSummary aiSummary;
    private List<String> recommendations;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Weather {
        private double temperature;
        private int humidity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AiSummary {
        private List<TimeRange> cautionSlots; //주의 시간대
        private List<TimeRange> dangerSlots; //위험 시간대
        private TimeRange peakRiskSlot; //위험도가 가장 높았던 시간대
        private double peakRiskScore; //가장 높았던 위험도
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeRange {
        private String startTime; //ex) "14:00"
        private String endTime;
    }
}
