package mallang.sallang.report.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReportRequestDto {
    // 위치 정보 및 레포트 생성 일자
    public Double lat;
    public Double lon;
    public LocalDate date;

    // 사용자 기본 정보
    public Integer height;
    public Integer weight;
    public String sex;
    public List<String> chronicConditions; // 기저 질환

    // 사용자 일일 정보
    public Integer steps;
    public Integer sleepMinutes;
    public Integer waterIntake; // 음수량
    public Integer workMinutes; // 총 업무 시간
    public Integer restMinutes; // 총 휴식 시간
    public List<String> medications; // 복용 약물

    public List<RestWindow> restWindows;
    public List<WorkWindow> workWindows;
    public List<RiskWindow> riskWindows;

    //휴식 시간대
    @Data
    public static class RestWindow {
        // "HH:mm" 형태로 받되, 서비스에서 LocalTime으로 파싱해 사용
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String start;
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String end;
    }

    @Data
    public static class WorkWindow {
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String start;
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String end;
    }

    @Data
    public static class RiskWindow {
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String start;
        @Pattern(regexp = "^[0-2]\\d:[0-5]\\d$") public String end;
        public Integer riskScore; // 0~100
        public String riskLabel; // 낮음/주의/위험 등
    }
}
