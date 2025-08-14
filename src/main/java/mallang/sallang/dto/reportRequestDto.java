package mallang.sallang.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class reportRequestDto {
    private Location location;
    private UserProfile userProfile;
    private DailyStats dailyStats;

    //보고서 작성 날짜
    private LocalDate reportDate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location { //위치 정보
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfile { //사용자 기본 정보
        private int age;
        private int height;
        private int weight;
        private Gender gender;
    }

    public enum Gender {MALE, FEMALE}

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyStats { //사용자 일일 정보
        private int totalSteps; //걸음 수
        private int totalWorkMinutes; //총 업무 시간
        private int totalRestMinutes; //총 휴식 시간
        private int waterIntake; //음수량
    }

}
