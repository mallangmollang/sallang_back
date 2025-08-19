package mallang.sallang.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mallang.sallang.dto.riskIfoDto.RiskInfoRequestDto;
import mallang.sallang.dto.riskIfoDto.RiskInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RiskInfoService {

    private final WeatherInfoService weatherInfoService;
    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public RiskInfoResponseDto calculateAndCreateResponse(RiskInfoRequestDto requestDto) {

        // ✅ 변경: lat / lon 사용
        JsonNode weatherData = weatherInfoService.getWeatherInfo(requestDto.getLat(), requestDto.getLon());
        double temperature = weatherInfoService.getTemperature(weatherData);
        int humidity = weatherInfoService.getHumidity(weatherData);
        int uvIndex = weatherInfoService.getUvIndex(weatherData);

        // GPT 한 번 호출로 점수 + AI 충고 가져오기
        HeatstrokeResult result = requestHeatstrokeScoreAndAdviceFromGPT(requestDto, temperature, humidity, uvIndex);

        // RealtimeRisk 객체 생성
        RiskInfoResponseDto.RealtimeRisk realtimeRisk = new RiskInfoResponseDto.RealtimeRisk();
        realtimeRisk.setTemperatureC(temperature);
        realtimeRisk.setHumidityPercentage(humidity);
        realtimeRisk.setUvIndex(uvIndex);
        realtimeRisk.setHeatstrokeRiskScore(result.score);

        // 응답 DTO 생성 후 반환
        return createResponseDto(realtimeRisk, result.advice);
    }

    // GPT 호출
    private String callGptApi(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");
            body.put("messages", Collections.singletonList(message));

            String jsonBody = objectMapper.writeValueAsString(body);

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            String response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    String.class
            );

            System.out.println("=== callGptApi Response ===");
            System.out.println(response);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"score\":0,\"advice\":\"충고를 생성할 수 없습니다.\"}";
        }
    }

    // GPT 한 번 호출로 점수 + 충고 가져오기
    private HeatstrokeResult requestHeatstrokeScoreAndAdviceFromGPT(RiskInfoRequestDto requestDto,
                                                                    double temperature,
                                                                    int humidity,
                                                                    int uvIndex) {
        // ✅ 변경: DTO 필드 직접 사용 (physicalInfo, currentActivity 제거됨)
        String prompt =
                "사용자 정보와 날씨를 기반으로 0~100 사이 열사병 위험 점수와 짧은 AI 충고를 JSON 형식으로 만들어주세요.\n" +
                        "JSON 키는 score(숫자), advice(문자열)로 해주세요.\n" +
                        "출력할 때 **다른 설명이나 텍스트는 절대 포함하지 말고**, 순수 JSON만 출력해주세요.\n" +
                        "나이: " + requestDto.getAge() + "\n" +
                        "걸음수: " + requestDto.getSteps() + "\n" +
                        "수분 섭취량: " + requestDto.getWaterIntake() + "ml\n" +
                        "휴식 시간: " + requestDto.getRestMinutes() + "분\n" +
                        "수면 시간: " + requestDto.getSleepMinutes() + "분\n" +
                        "온도: " + temperature + "\n" +
                        "습도: " + humidity + "\n" +
                        "UV 지수: " + uvIndex + "\n" +
                        "JSON만 출력해주세요. { 로 시작하고 } 로 끝나게 해줘. " +
                        "충고할 때 개킹받는 오덕말투로 해줘. 앞에 json 같은 글씨 쓰지 말고 그냥 JSON 형식만 지켜.";

        System.out.println("=== GPT Prompt ===");
        System.out.println(prompt);

        String response = callGptApi(prompt);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isMissingNode() || !choicesNode.isArray() || choicesNode.size() == 0) {
                System.out.println("⚠️ choices 배열이 없거나 비어 있음");
                return new HeatstrokeResult(random.nextInt(50) + 30, "충고를 생성할 수 없습니다.");
            }

            String content = choicesNode.get(0).path("message").path("content").asText();

            // 🔹 GPT가 반환한 JSON에서 backtick(`) 제거 + trim
            content = content.replace("`", "").trim();

            System.out.println("=== Clean GPT Content ===");
            System.out.println(content);

            JsonNode contentJson = objectMapper.readTree(content);
            int score = contentJson.path("score").asInt(random.nextInt(50) + 30);
            String advice = contentJson.path("advice").asText("충고를 생성할 수 없습니다.");
            return new HeatstrokeResult(score, advice);

        } catch (Exception e) {
            e.printStackTrace();
            return new HeatstrokeResult(random.nextInt(50) + 30, "충고를 생성할 수 없습니다.");
        }
    }

    // 결과를 담을 클래스
    private static class HeatstrokeResult {
        int score;
        String advice;

        public HeatstrokeResult(int score, String advice) {
            this.score = score;
            this.advice = advice;
        }
    }



    // 여기서만 쓰는 DTO로 따로 파일을 안만듦
    // DTO 생성
    public RiskInfoResponseDto createResponseDto(RiskInfoResponseDto.RealtimeRisk realtimeRisk, String aiAdvice) {
        RiskInfoResponseDto responseDto = new RiskInfoResponseDto();
        responseDto.setStatus("success");
        responseDto.setMessage("작업이 시작되었습니다. 실시간 위험도를 확인하세요.");
        responseDto.setAi_advice(aiAdvice);

        RiskInfoResponseDto.Data data = new RiskInfoResponseDto.Data();
        data.setRealtimeRisk(realtimeRisk);
        responseDto.setData(data);

        return responseDto;
    }

//
//    public int calculateHeatstrokeRiskScore(RiskInfoRequestDto requestDto, double temperature, int humidity, int uvIndex) {
//        throw new UnsupportedOperationException("GPT 기반 점수 계산으로 대체됨");
//    }

    public String getAiAdvice() {
        throw new UnsupportedOperationException("GPT 기반 AI 조언으로 대체됨");
    }
}
