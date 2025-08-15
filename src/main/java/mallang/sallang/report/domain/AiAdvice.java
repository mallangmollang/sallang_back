package mallang.sallang.report.domain;

import java.util.List;

public record AiAdvice(Scores scores,
                       List<String> analysisComments,
                       List<String> tomorrowSuggestions) {
    public record Scores(int hydration, int rest, int symptom) {}
}
