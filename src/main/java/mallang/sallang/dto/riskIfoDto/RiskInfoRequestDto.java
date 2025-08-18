package mallang.sallang.dto.riskIfoDto;

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

    public static class PhysicalInfo {
        private int age;
        private int weightKg;
        private int heightCm;
        private String gender;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getWeightKg() {
            return weightKg;
        }

        public void setWeightKg(int weightKg) {
            this.weightKg = weightKg;
        }

        public int getHeightCm() {
            return heightCm;
        }

        public void setHeightCm(int heightCm) {
            this.heightCm = heightCm;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public static class CurrentActivity {
        private int stepsCount;
        private int waterIntakeMl;
        private String mealStatus;
        private int restDurationMinutes;

        public int getStepsCount() {
            return stepsCount;
        }

        public void setStepsCount(int stepsCount) {
            this.stepsCount = stepsCount;
        }

        public int getWaterIntakeMl() {
            return waterIntakeMl;
        }

        public void setWaterIntakeMl(int waterIntakeMl) {
            this.waterIntakeMl = waterIntakeMl;
        }

        public String getMealStatus() {
            return mealStatus;
        }

        public void setMealStatus(String mealStatus) {
            this.mealStatus = mealStatus;
        }

        public int getRestDurationMinutes() {
            return restDurationMinutes;
        }

        public void setRestDurationMinutes(int restDurationMinutes) {
            this.restDurationMinutes = restDurationMinutes;
        }
    }
}