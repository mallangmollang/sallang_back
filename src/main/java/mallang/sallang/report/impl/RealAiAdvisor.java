package mallang.sallang.report.impl;

import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.port.HealthAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!fake")
public class RealAiAdvisor implements HealthAdvisor{

    private final ChatClient chatClient;

    public RealAiAdvisor(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public AiAdvice analyze(ReportRequestDto req, WeatherData weather) {
        // 요청/날씨 요약 문자열 생성
        String requestBlock = summarizeReq(req);
        String weatherBlock = summarizeWeather(weather);

        // 프롬프트 문자열
        String promptText = """
            너는 온열질환 예방 코치다. 아래 입력을 바탕으로 조언을 생성하되,
            결과는 반드시 JSON(오직 JSON)으로만 출력하라. 마크다운/코드펜스/주석 금지.
            수분 섭취량, 휴식 시간 등을 고려해 hydration, rest, symptom 점수를 산정. 높을수록 온열 질환 예방을 잘한 것.
            위험도가 높았던 시간대와 휴식 시간대가 10분 이상 겹치지 않는 경우이거나 수분 섭취량이 권장량 미만인 경우
            tomorrowSuggestions에 그것과 관련된 제안을 추가할 것.
            analysisComments와 tomorrowSuggestions는 각각 최대 3개까지이며,
            각 그룹별 3개의 조언들은 가능한 서로 다른 내용.
            
            

            [요청]
            %s

            [날씨]
            %s

            JSON 스키마(AiAdvice와 정확히 일치):
            {
              "scores": { "hydration": number, "rest": number, "symptom": number },
              "analysisComments": ["..."],
              "tomorrowSuggestions": ["..."]
            }
            """.formatted(requestBlock, weatherBlock);

        // Spring AI 호출
        try {
            return chatClient.prompt(promptText)
                    .call()
                    .entity(AiAdvice.class);
        } catch (Exception e) {
            // 실패 시 fallback
            return fallback();
        }
    }

    private String summarizeReq(ReportRequestDto req) {
        return """
            - 키: %dcm, 몸무게: %dkg, 성별: %s
            - 기저질환: %s
            - 복용약: %s
            - 걸음 수: %d, 수면: %d분
            - 수분 섭취: %dml
            - 업무시간: %d분, 휴식시간: %d분
            - 휴식 구간: %s
            - 위험 구간: %s
            """.formatted(
                req.getHeight(), req.getWeight(), req.getSex(),
                String.join(", ", req.getChronicConditions()),
                String.join(", ", req.getMedications()),
                req.getSteps(), req.getSleepMinutes(),
                req.getWaterIntake(), req.getWorkMinutes(), req.getRestMinutes(),
                req.getRestWindows(), req.getRiskWindows()
        );
    }

    private String summarizeWeather(WeatherData weather) {
        return """
            - 기온: %.1f°C
            - 습도: %d%%
            """.formatted(weather.temperature(), weather.humidity());
    }

    private static AiAdvice fallback() {
        return new AiAdvice(
                new AiAdvice.Scores(50, 50, 50),
                List.of("AI 분석에 실패하여 기본 가이드를 제공합니다.",
                        "수분 섭취를 평소보다 자주 하고, 한낮에는 그늘에서 짧은 휴식을 취하세요."),
                List.of("내일은 작업 시작 전 충분한 수분 섭취를 준비하세요.",
                        "13~16시 사이에는 10~15분의 그늘 휴식을 1회 이상 확보하세요.")
        );
    }
}
