package UrbanDrainageNetworkOptimization_ACO_Approach1;
import java.util.Map;
public class DesignRainfall {
    private final int returnPeriod; 
    private final Map<Integer, Double> intensityDurationCurve; 
    public DesignRainfall(int returnPeriod, Map<Integer, Double> intensityDurationCurve) {
        this.returnPeriod = returnPeriod;
        this.intensityDurationCurve = intensityDurationCurve;
    }
    public double getIntensityForDuration(int durationMinutes) {
        Integer lower = null, upper = null;
        for (Integer d : intensityDurationCurve.keySet()) {
            if (d <= durationMinutes && (lower == null || d > lower)) {
                lower = d;
            }
            if (d >= durationMinutes && (upper == null || d < upper)) {
                upper = d;
            }
        }
        if (lower == null && upper == null) {
            throw new IllegalArgumentException("No rainfall data available");
        }
        if (lower == null) return intensityDurationCurve.get(upper);
        if (upper == null) return intensityDurationCurve.get(lower);
        if (lower == upper) return intensityDurationCurve.get(lower);
        double intensityLower = intensityDurationCurve.get(lower);
        double intensityUpper = intensityDurationCurve.get(upper);
        return intensityLower + (intensityUpper - intensityLower) * 
            (durationMinutes - lower) / (upper - lower);
    }
    public int getReturnPeriod() { 
        return returnPeriod; 
    }
    public Map<Integer, Double> getIntensityDurationCurve() { 
        return intensityDurationCurve; 
    }
}