package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class Pipe {
    public final int fromNode;
    public final int toNode;
    public final double length;
    public final double initialDiameter;
    public Pipe(int fromNode, int toNode, double length, double initialDiameter) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.length = length;
        this.initialDiameter = initialDiameter;
    }
}