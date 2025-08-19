//package mallang.sallang.dto.riskIfoDto;
//
//import java.util.List;
//
//public class RiskInfoRequestDto {
//    private double latitude;
//    private double longitude;
//    private PhysicalInfo physicalInfo;
//    private CurrentActivity currentActivity;
//
//    public double getLatitude() {
//        return latitude;
//    }
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
//    public PhysicalInfo getPhysicalInfo() {
//        return physicalInfo;
//    }
//    public void setPhysicalInfo(PhysicalInfo physicalInfo) {
//        this.physicalInfo = physicalInfo;
//    }
//
//    public CurrentActivity getCurrentActivity() {
//        return currentActivity;
//    }
//    public void setCurrentActivity(CurrentActivity currentActivity) {
//        this.currentActivity = currentActivity;
//    }
//
//    // ✅ 신체 정보
//    public static class PhysicalInfo {
//        private int age;
//        private int weightKg;
//        private int heightCm;
//        private String gender;
//        private List<String> chronicDiseases; // ✅ 기저질환
//
//        public int getAge() { return age; }
//        public void setAge(int age) { this.age = age; }
//
//        public int getWeightKg() { return weightKg; }
//        public void setWeightKg(int weightKg) { this.weightKg = weightKg; }
//
//        public int getHeightCm() { return heightCm; }
//        public void setHeightCm(int heightCm) { this.heightCm = heightCm; }
//
//        public String getGender() { return gender; }
//        public void setGender(String gender) { this.gender = gender; }
//
//        public List<String> getChronicDiseases() { return chronicDiseases; }
//        public void setChronicDiseases(List<String> chronicDiseases) { this.chronicDiseases = chronicDiseases; }
//    }
//
//    // ✅ 활동 정보
//    public static class CurrentActivity {
//        private int stepsCount;
//        private int waterIntakeMl;
//        private String mealStatus;
//        private int restDurationMinutes;
//        private int sleepHours; // ✅ 수면시간
//
//        public int getStepsCount() { return stepsCount; }
//        public void setStepsCount(int stepsCount) { this.stepsCount = stepsCount; }
//
//        public int getWaterIntakeMl() { return waterIntakeMl; }
//        public void setWaterIntakeMl(int waterIntakeMl) { this.waterIntakeMl = waterIntakeMl; }
//
//        public String getMealStatus() { return mealStatus; }
//        public void setMealStatus(String mealStatus) { this.mealStatus = mealStatus; }
//
//        public int getRestDurationMinutes() { return restDurationMinutes; }
//        public void setRestDurationMinutes(int restDurationMinutes) { this.restDurationMinutes = restDurationMinutes; }
//
//        public int getSleepHours() { return sleepHours; }
//        public void setSleepHours(int sleepHours) { this.sleepHours = sleepHours; }
//    }
//}
package mallang.sallang.dto.riskIfoDto;

import java.util.List;

public class RiskInfoRequestDto {
    private double lat;   // 위도
    private double lon;   // 경도
    private int age;
    private int weight;
    private int height;
    private String sex;
    private List<String> chronicConditions;

    // ✅ 활동 정보도 전부 최상위 필드로 이동
    private int steps;
    private int waterIntake;
    private int restMinutes;
    private int sleepMinutes;

    // Getters & Setters
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public List<String> getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(List<String> chronicConditions) { this.chronicConditions = chronicConditions; }

    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }

    public int getWaterIntake() { return waterIntake; }
    public void setWaterIntake(int waterIntake) { this.waterIntake = waterIntake; }

    public int getRestMinutes() { return restMinutes; }
    public void setRestMinutes(int restMinutes) { this.restMinutes = restMinutes; }

    public int getSleepMinutes() { return sleepMinutes; }
    public void setSleepMinutes(int sleepMinutes) { this.sleepMinutes = sleepMinutes; }
}
