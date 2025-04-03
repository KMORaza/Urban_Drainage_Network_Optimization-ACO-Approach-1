package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class DrainagePipe {
    private final int fromNode;
    private final int toNode;
    private final double length;
    private final PipeMaterial material;
    public DrainagePipe(int fromNode, int toNode, double length, PipeMaterial material) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.length = length;
        this.material = material;
    }
    public int getFromNode() { 
        return fromNode; 
    }
    public int getToNode() { 
        return toNode; 
    }
    public double getLength() { 
        return length; 
    }
    public PipeMaterial getMaterial() { 
        return material; 
    }
    public double calculateInstallationCost(double diameter) {
        double baseCost = material.getCostPerMeter(diameter);
        double depth = 1.0 + diameter * 2.0;
        double excavationCost = 50.0 * depth;
        return (baseCost + excavationCost) * length;
    }
}