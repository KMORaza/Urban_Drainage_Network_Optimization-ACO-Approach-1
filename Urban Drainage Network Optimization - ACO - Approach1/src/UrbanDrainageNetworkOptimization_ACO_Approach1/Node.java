package UrbanDrainageNetworkOptimization_ACO_Approach1;
public class Node {
    public final int id;
    public final double x, y;
    public final double elevation;
    public final double drainageArea;
    public Node(int id, double x, double y, double elevation, double drainageArea) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.drainageArea = drainageArea;
    }
    public Node(int id, double x, double elevation, double drainageArea) {
        this(id, x, 0, elevation, drainageArea);
    }
}