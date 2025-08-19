package mallang.sallang.dto.riskIfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("temperature")  // JSON 응답에는 "temperature"
        private double temperatureC;  // 내부 필드는 temperatureC 그대로 사용
        private int humidityPercentage;
        private int uvIndex;
        private int heatstrokeRiskScore;

    }
}