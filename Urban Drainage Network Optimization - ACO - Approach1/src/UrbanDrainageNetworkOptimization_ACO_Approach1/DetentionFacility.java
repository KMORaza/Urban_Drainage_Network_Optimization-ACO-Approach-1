package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class DetentionFacility {
    private final int id;
    private final String name;
    private final double capacity; 
    private final double releaseRate; 
    private final double depth;
    private final boolean isDetention; 
    public DetentionFacility(int id, String name, double capacity, 
                           double releaseRate, double depth, boolean isDetention) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.releaseRate = releaseRate;
        this.depth = depth;
        this.isDetention = isDetention;
    }
    public int getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public double getCapacity() { 
        return capacity; 
    }
    public double getReleaseRate() { 
        return releaseRate; 
    }
    public double getDepth() { 
        return depth; 
    }
    public boolean isDetention() { 
        return isDetention; 
    }
    public double getCost() {
        double baseCost = isDetention ? 50000 : 75000;
        return baseCost + capacity * (isDetention ? 50 : 75);
    }
}