package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class CatchmentArea {
    private final int id;
    private final String name;
    private final double area; 
    private final double runoffCoefficient;
    private final Topography topography;
    private final SoilType soilType;
    private final LandUse landUse;
    public CatchmentArea(int id, String name, double area, double runoffCoefficient,
                       Topography topography, SoilType soilType, LandUse landUse) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.runoffCoefficient = runoffCoefficient;
        this.topography = topography;
        this.soilType = soilType;
        this.landUse = landUse;
    }
    public int getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public double getArea() { 
        return area; 
    }
    public double getRunoffCoefficient() { 
        return runoffCoefficient; 
    }
    public Topography getTopography() { 
        return topography; 
    }
    public SoilType getSoilType() { 
        return soilType; 
    }
    public LandUse getLandUse() { 
        return landUse; 
    }
    public double calculateTimeOfConcentration() {
        double length = Math.sqrt(area) / 2;
        double slope = topography.getAverageSlope();
        return 0.02 * Math.pow(length, 0.77) * Math.pow(slope, -0.385);
    }
}