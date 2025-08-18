package mallang.sallang.riskInfo.dto;

import java.util.List;

public class RiskInfoRequestDto {
    private double latitude;
    private double longitude;
    private PhysicalInfo physicalInfo;
    private CurrentActivity currentActivity;

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public PhysicalInfo getPhysicalInfo() {
        return physicalInfo;
    }
    public void setPhysicalInfo(PhysicalInfo physicalInfo) {
        this.physicalInfo = physicalInfo;
    }

    public CurrentActivity getCurrentActivity() {
        return currentActivity;
    }
    public void setCurrentActivity(CurrentActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    // ✅ 신체 정보
    public static class PhysicalInfo {
        private int age;
        private int weightKg;
        private int heightCm;
        private String gender;
        private List<String> chronicDiseases; // ✅ 기저질환

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public int getWeightKg() { return weightKg; }
        public void setWeightKg(int weightKg) { this.weightKg = weightKg; }

        public int getHeightCm() { return heightCm; }
        public void setHeightCm(int heightCm) { this.heightCm = heightCm; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public List<String> getChronicDiseases() { return chronicDiseases; }
        public void setChronicDiseases(List<String> chronicDiseases) { this.chronicDiseases = chronicDiseases; }
    }

    // ✅ 활동 정보
    public static class CurrentActivity {
        private int stepsCount;
        private int waterIntakeMl;
        private String mealStatus;
        private int restDurationMinutes;
        private int sleepHours; // ✅ 수면시간

        public int getStepsCount() { return stepsCount; }
        public void setStepsCount(int stepsCount) { this.stepsCount = stepsCount; }

        public int getWaterIntakeMl() { return waterIntakeMl; }
        public void setWaterIntakeMl(int waterIntakeMl) { this.waterIntakeMl = waterIntakeMl; }

        public String getMealStatus() { return mealStatus; }
        public void setMealStatus(String mealStatus) { this.mealStatus = mealStatus; }

        public int getRestDurationMinutes() { return restDurationMinutes; }
        public void setRestDurationMinutes(int restDurationMinutes) { this.restDurationMinutes = restDurationMinutes; }

        public int getSleepHours() { return sleepHours; }
        public void setSleepHours(int sleepHours) { this.sleepHours = sleepHours; }
    }
}
