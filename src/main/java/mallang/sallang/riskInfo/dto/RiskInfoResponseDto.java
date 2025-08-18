package mallang.sallang.riskInfo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskInfoResponseDto {
    private String status;
    private String message;
    private Data data;
    private String ai_advice;

    public static class Data {
        private RealtimeRisk realtimeRisk;

        public RealtimeRisk getRealtimeRisk() {
            return realtimeRisk;
        }

        public void setRealtimeRisk(RealtimeRisk realtimeRisk) {
            this.realtimeRisk = realtimeRisk;
        }
    }

    @Getter
    @Setter
    public static class RealtimeRisk {
        private double temperatureC;
        private int humidityPercentage;
        private int uvIndex;
        private int heatstrokeRiskScore;

    }
}