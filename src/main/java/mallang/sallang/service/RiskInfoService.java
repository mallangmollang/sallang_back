package mallang.sallang.service;

import lombok.RequiredArgsConstructor;
import mallang.sallang.dto.riskIfoDto.RiskInfoRequestDto;
import mallang.sallang.dto.riskIfoDto.RiskInfoResponseDto;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RiskInfoService {

    private final WeatherInfoService weatherInfoService;
    private final Random random = new Random();

    public RiskInfoResponseDto calculateAndCreateResponse(RiskInfoRequestDto requestDto) {

        // 1️⃣ 날씨 정보 가져오기
        JsonNode weatherData = weatherInfoService.getWeatherInfo(requestDto.getLatitude(), requestDto.getLongitude());
        double temperature = weatherInfoService.getTemperature(weatherData);
        int humidity = weatherInfoService.getHumidity(weatherData);
        int uvIndex = weatherInfoService.getUvIndex(weatherData);

        // 2️⃣ 위험도 계산
        int heatstrokeRiskScore = calculateHeatstrokeRiskScore(requestDto, temperature, humidity, uvIndex);

        // 3️⃣ RealtimeRisk 객체 생성
        RiskInfoResponseDto.RealtimeRisk realtimeRisk = new RiskInfoResponseDto.RealtimeRisk();
        realtimeRisk.setTemperatureC(temperature);
        realtimeRisk.setHumidityPercentage(humidity);
        realtimeRisk.setUvIndex(uvIndex);
        realtimeRisk.setHeatstrokeRiskScore(heatstrokeRiskScore);

        // 4️⃣ 응답 DTO 생성 후 반환
        return createResponseDto(realtimeRisk);
    }

    public int calculateHeatstrokeRiskScore(RiskInfoRequestDto requestDto, double temperature, int humidity, int uvIndex) {
        int baseScore = 30;
        int ageFactor = requestDto.getPhysicalInfo().getAge() / 10;
        int stepsFactor = requestDto.getCurrentActivity().getStepsCount() / 500;

        // 간단한 가중치 예시 (온도, 습도, UV 반영)
        int weatherFactor = (int) (temperature / 5) + (humidity / 20) + uvIndex;

        int randomAdjustment = random.nextInt(20);

        return Math.min(100, baseScore + ageFactor + stepsFactor + weatherFactor + randomAdjustment);
    }

    public String getAiAdvice() {
        String[] advices = {
                "잠시 수분 섭취를 추천드려요!",
                "휴식을 취하고 몸의 열을 식히세요.",
                "그늘에서 잠시 쉬어가는 건 어떨까요?",
                "햇볕이 강하니 모자를 착용하세요."
        };
        return advices[random.nextInt(advices.length)];
    }

    public RiskInfoResponseDto createResponseDto(RiskInfoResponseDto.RealtimeRisk realtimeRisk) {
        RiskInfoResponseDto responseDto = new RiskInfoResponseDto();
        responseDto.setStatus("success");
        responseDto.setMessage("작업이 시작되었습니다. 실시간 위험도를 확인하세요.");
        responseDto.setAi_advice(getAiAdvice());

        RiskInfoResponseDto.Data data = new RiskInfoResponseDto.Data();
        data.setRealtimeRisk(realtimeRisk);
        responseDto.setData(data);

        return responseDto;
    }
}