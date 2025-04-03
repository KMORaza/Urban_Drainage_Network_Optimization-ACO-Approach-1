package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class WaterQualityRequirements {
    private final double maxTSS; 
    private final double maxOilGrease; 
    private final double minPH;
    private final double maxPH;
    private final boolean requiresSedimentation;
    public WaterQualityRequirements(double maxTSS, double maxOilGrease, 
                                  double minPH, double maxPH, 
                                  boolean requiresSedimentation) {
        this.maxTSS = maxTSS;
        this.maxOilGrease = maxOilGrease;
        this.minPH = minPH;
        this.maxPH = maxPH;
        this.requiresSedimentation = requiresSedimentation;
    }
    public double getMaxTSS() { 
        return maxTSS; 
    }
    public double getMaxOilGrease() { 
        return maxOilGrease; 
    }
    public double getMinPH() { 
        return minPH; 
    }
    public double getMaxPH() { 
        return maxPH; 
    }
    public boolean requiresSedimentation() { 
        return requiresSedimentation; 
    }
}