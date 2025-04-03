package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class LandUse {
    private final double percentImpervious;
    private final double percentLawn;
    private final double percentOther;
    public LandUse(double percentImpervious, double percentLawn, double percentOther) {
        this.percentImpervious = percentImpervious;
        this.percentLawn = percentLawn;
        this.percentOther = percentOther;
    }
    public double getPercentImpervious() { 
        return percentImpervious; 
    }
    public double getPercentLawn() { 
        return percentLawn; 
    }
    public double getPercentOther() { 
        return percentOther; 
    }
    public double calculateCompositeRunoffCoefficient() {
        return (percentImpervious * 0.9 + percentLawn * 0.2 + percentOther * 0.5) / 100;
    }
}