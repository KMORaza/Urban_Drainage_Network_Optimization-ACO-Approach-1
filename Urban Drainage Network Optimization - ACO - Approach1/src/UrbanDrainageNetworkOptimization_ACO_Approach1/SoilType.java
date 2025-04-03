package UrbanDrainageNetworkOptimization_ACO_Approach1;
public enum SoilType {
    SAND(25.0, "Sand"),
    LOAM(12.5, "Loam"),
    CLAY(5.0, "Clay"),
    URBAN(2.5, "Urban"),
    ROCK(1.0, "Rock");
    private final double infiltrationRate; 
    private final String displayName;
    SoilType(double infiltrationRate, String displayName) {
        this.infiltrationRate = infiltrationRate;
        this.displayName = displayName;
    }
    public double getInfiltrationRate() {
        return infiltrationRate;
    }
    public String getDisplayName() {
        return displayName;
    }
}