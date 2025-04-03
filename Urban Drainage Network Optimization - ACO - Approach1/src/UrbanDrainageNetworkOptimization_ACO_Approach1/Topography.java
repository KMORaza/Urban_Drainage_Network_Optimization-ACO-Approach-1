package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class Topography {
    private final double elevation;
    private final double outletElevation;
    private final double averageSlope; 
    public Topography(double elevation, double outletElevation, double averageSlope) {
        this.elevation = elevation;
        this.outletElevation = outletElevation;
        this.averageSlope = averageSlope;
    }
    public double getElevation() { 
        return elevation; 
    }
    public double getOutletElevation() { 
        return outletElevation; 
    }
    public double getAverageSlope() { 
        return averageSlope; 
    }
}