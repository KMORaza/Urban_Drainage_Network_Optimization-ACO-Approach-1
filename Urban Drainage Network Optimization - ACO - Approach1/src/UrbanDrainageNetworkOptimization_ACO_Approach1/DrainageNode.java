package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class DrainageNode {
    private final int id;
    private final String name;
    private final double elevation;
    private final double drainageArea;
    private final CatchmentArea catchment;
    public DrainageNode(int id, String name, double elevation, 
                       double drainageArea, CatchmentArea catchment) {
        this.id = id;
        this.name = name;
        this.elevation = elevation;
        this.drainageArea = drainageArea;
        this.catchment = catchment;
    }    
    public int getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public double getElevation() { 
        return elevation; 
    }
    public double getDrainageArea() { 
        return drainageArea; 
    }
    public CatchmentArea getCatchment() { 
        return catchment; 
    }
}