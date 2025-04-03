package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class RegulatoryStandards {
    private final double maxVelocity; 
    private final double minSlope; 
    private final double minCover; 
    private final int designReturnPeriod;
    public RegulatoryStandards(double maxVelocity, double minSlope, 
                             double minCover, int designReturnPeriod) {
        this.maxVelocity = maxVelocity;
        this.minSlope = minSlope;
        this.minCover = minCover;
        this.designReturnPeriod = designReturnPeriod;
    }
    public double getMaxVelocity() { 
        return maxVelocity; 
    }
    public double getMinSlope() { 
        return minSlope; 
    }
    public double getMinCover() { 
        return minCover; 
    }
    public int getDesignReturnPeriod() { 
        return designReturnPeriod; 
    }
}