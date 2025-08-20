
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
