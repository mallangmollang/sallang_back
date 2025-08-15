package mallang.sallang.report.service;

import mallang.sallang.report.domain.AiAdvice;
import mallang.sallang.report.domain.WeatherData;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.port.HealthAdvisor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("fake")
public class FakeAiAdvisor implements HealthAdvisor {
    private static final int HIGH_RISK_THRESHOLD = 70;      // 고위험
    private static final int VERY_HIGH_RISK_THRESHOLD = 85; // 매우 높은 위험(코멘트 강조용)

    @Override
    public AiAdvice analyze(ReportRequestDto req, WeatherData dailyWeather) {
        // 1) 점수 산정(기존 로직 재사용 가능)
        int hydration = calcHydrationScore(req.waterIntake, req.weight);
        int rest = calcRestScore(req.workMinutes, req.restMinutes);
        int symptom = calcSymptomScoreByRisk(req); // 🔁 날씨 대신 risk 기반으로 가중치

        AiAdvice.Scores scores = new AiAdvice.Scores(hydration, rest, symptom);

        // 2) 사후 위험 시간대 분석(리스트 병합/휴식 여부 판정)
        List<Slot> riskyNoRest = analyzeHighRiskNoRest(req);

        // 3) 분석 코멘트
        List<String> comments = new ArrayList<>();
        for (Slot s : riskyNoRest) {
            // 매우 높은 위험이면 강조
            boolean veryHigh = s.maxRisk >= VERY_HIGH_RISK_THRESHOLD;
            comments.add(
                    String.format(
                            "오늘 %s~%s 구간은 위험도 %d~%d(고위험)%s였으나 휴식이 없었습니다.",
                            s.start, s.end, s.minRisk, s.maxRisk,
                            veryHigh ? " [매우 높음]" : ""
                    )
            );
        }
        if (hydration < 70)
            comments.add("수분 섭취 부족: 체중 대비 목표치보다 낮습니다. 소량씩 자주 마셔 주세요.");
        if (rest < 70)
            comments.add("휴식 관리 미흡: 노동 4시간당 최소 30분 휴식을 목표로 하세요.");

        // 4) 내일 제안(유사 조건 대비)
        List<String> suggestions = new ArrayList<>();
        if (!riskyNoRest.isEmpty()) {
            String merged = riskyNoRest.stream()
                    .map(s -> s.start + "~" + s.end)
                    .collect(Collectors.joining(", "));
            suggestions.add("내일 유사 조건 발생 시 " + merged + " 시간대에 10분 이상 그늘/실내 휴식을 권장합니다.");
        }
        if (hydration < 80)
            suggestions.add("내일은 수분 알림 간격을 45분 → 30분으로 단축해 보세요.");
        if (symptom < 80)
            suggestions.add("기저질환/복용약물과 높은 위험도 조합에 유의하세요. 어지러움/탈수 시 즉시 휴식.");

        return new AiAdvice(scores, comments, suggestions);
    }

    /* ------------------ 점수 계산 ------------------ */

    private int calcHydrationScore(int intakeMl, int weightKg) {
        double goal = weightKg * 30.0;
        double pct = Math.max(0, Math.min(100, Math.round(intakeMl / goal * 100)));
        return (int) pct;
    }

    private int calcRestScore(int workMin, int restMin) {
        if (workMin == 0) return 100;
        double base = Math.min(100, (restMin / (workMin / 240.0 * 30.0)) * 100.0); // 4h당 30m
        return (int) Math.max(0, Math.min(100, Math.round(base)));
    }

    // 🔁 증상 점수를 '오늘의 위험도 프로파일'로 가중
    private int calcSymptomScoreByRisk(ReportRequestDto req) {
        int score = 100;
        if (req.riskWindows == null || req.riskWindows.isEmpty()) return score;

        // 최대 위험도가 높을수록 점수 페널티
        int maxRisk = req.riskWindows.stream()
                .filter(r -> r != null && r.riskScore != null)
                .mapToInt(r -> r.riskScore)
                .max().orElse(0);

        if (maxRisk >= VERY_HIGH_RISK_THRESHOLD) score -= 10;
        else if (maxRisk >= HIGH_RISK_THRESHOLD) score -= 5;

        // 기저질환/약물에 추가 페널티
        if (contains(req.medications, "이뇨제")) score -= 10;
        if (contains(req.chronicConditions, "고혈압")) score -= 5;
        if (contains(req.chronicConditions, "당뇨")) score -= 5;
        if (req.workMinutes != null && req.workMinutes >= 480) score -= 5;

        return Math.max(0, score);
    }

    private boolean contains(List<String> list, String token) {
        if (list == null) return false;
        for (String s : list) {
            if (s != null && s.contains(token)) return true;
        }
        return false;
    }

    /* ------------------ 위험도 기반 사후 분석 ------------------ */

    private List<Slot> analyzeHighRiskNoRest(ReportRequestDto req) {
        if (req.riskWindows == null || req.riskWindows.isEmpty()) return List.of();

        // 1) 업무 구간(있으면 제한, 없으면 09:00~workMinutes 가정)
        List<Slot> work = buildWorkSlots(req);

        // 2) 위험 슬롯(임계 이상만) 생성
        List<Slot> high = new ArrayList<>();
        for (ReportRequestDto.RiskWindow rw : req.riskWindows) {
            if (rw == null || rw.riskScore == null) continue;
            if (rw.riskScore < HIGH_RISK_THRESHOLD) continue; // 임계 미만은 skip
            LocalTime s = LocalTime.parse(rw.start);
            LocalTime e = LocalTime.parse(rw.end);
            high.add(new Slot(s, e, rw.riskScore, rw.riskScore));
        }
        if (high.isEmpty()) return List.of();

        // 3) 업무 구간 내부로 클리핑
        List<Slot> clipped = intersectWith(work, high);

        // 4) 휴식과 겹치는 위험 구간 제거
        List<Slot> noRest = excludeRestWithThreshold(req.restWindows, clipped, 10);

        // 5) 인접·겹치는 슬롯 병합 (risk min/max 유지)
        return mergeAdjacent(noRest);
    }

    private List<Slot> buildWorkSlots(ReportRequestDto req) {
        List<Slot> out = new ArrayList<>();
        if (req.workWindows == null || req.workWindows.isEmpty()) {
            LocalTime s = LocalTime.of(9, 0);
            LocalTime e = s.plusMinutes(req.workMinutes == null ? 0 : req.workMinutes);
            if (!e.isBefore(s)) out.add(new Slot(s, e, null, null));
            return out;
        }
        for (ReportRequestDto.WorkWindow w : req.workWindows) {
            LocalTime s = LocalTime.parse(w.start);
            LocalTime e = LocalTime.parse(w.end);
            if (!e.isBefore(s)) out.add(new Slot(s, e, null, null));
        }
        return out;
    }

    private List<Slot> intersectWith(List<Slot> work, List<Slot> high) {
        List<Slot> out = new ArrayList<>();
        for (Slot w : work) {
            for (Slot h : high) {
                LocalTime s = timeMax(w.start, h.start);
                LocalTime e = timeMin(w.end, h.end);
                if (!e.isBefore(s)) {
                    out.add(new Slot(s, e, h.minRisk, h.maxRisk));
                }
            }
        }
        return out;
    }

    private List<Slot> excludeRestWithThreshold(List<ReportRequestDto.RestWindow> rests,
                                                List<Slot> slots, int minOverlapMinutes) {
        if (rests == null || rests.isEmpty()) return slots;
        List<Slot> out = new ArrayList<>();
        for (Slot s : slots) {
            int maxOverlap = 0;
            for (ReportRequestDto.RestWindow r : rests) {
                LocalTime rs = LocalTime.parse(r.start);
                LocalTime re = LocalTime.parse(r.end);
                LocalTime start = s.start.isAfter(rs) ? s.start : rs;
                LocalTime end   = s.end.isBefore(re) ? s.end : re;
                if (!end.isBefore(start)) {
                    int overlap = end.toSecondOfDay() - start.toSecondOfDay();
                    maxOverlap = Math.max(maxOverlap, overlap/60);
                }
            }
            if (maxOverlap < minOverlapMinutes) { // 커버리지 부족 → 경고 유지
                out.add(s);
            }
        }
        return out;
    }

    private List<Slot> mergeAdjacent(List<Slot> slots) {
        if (slots.isEmpty()) return slots;
        slots.sort(Comparator.comparing(a -> a.start));
        List<Slot> out = new ArrayList<>();
        Slot cur = slots.get(0);

        for (int i = 1; i < slots.size(); i++) {
            Slot nxt = slots.get(i);

            if (!nxt.start.isAfter(cur.end)) { // 겹침
                cur = new Slot(
                        cur.start,
                        timeMax(cur.end, nxt.end),
                        riskMin(cur.minRisk, nxt.minRisk),
                        riskMax(cur.maxRisk, nxt.maxRisk)
                );
            } else if (cur.end.equals(nxt.start)) { // 인접
                cur = new Slot(
                        cur.start,
                        nxt.end,
                        riskMin(cur.minRisk, nxt.minRisk),
                        riskMax(cur.maxRisk, nxt.maxRisk)
                );
            } else {
                out.add(cur);
                cur = nxt;
            }
        }
        out.add(cur);
        return out;
    }

    /* ------------------ 유틸 ------------------ */
    private LocalTime timeMax(LocalTime a, LocalTime b) { return a.isAfter(b) ? a : b; }
    private LocalTime timeMin(LocalTime a, LocalTime b) { return a.isBefore(b) ? a : b; }

    // ---- Integer(위험도) 전용 유틸
    private Integer riskMin(Integer a, Integer b) {
        if (a == null) return b;
        if (b == null) return a;
        return Math.min(a, b);
    }
    private Integer riskMax(Integer a, Integer b) {
        if (a == null) return b;
        if (b == null) return a;
        return Math.max(a, b);
    }

    private static class Slot {
        final LocalTime start, end;
        final Integer minRisk; // 슬랏 병합 시 최소 위험도
        final Integer maxRisk; // 슬랏 병합 시 최대 위험도
        Slot(LocalTime s, LocalTime e, Integer minRisk, Integer maxRisk) {
            this.start = s; this.end = e; this.minRisk = minRisk; this.maxRisk = maxRisk;
        }
    }
}
