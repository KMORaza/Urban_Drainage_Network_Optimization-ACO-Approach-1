package UrbanDrainageNetworkOptimization_ACO_Approach1;
import java.util.*;
public class DrainageSolution {
    private final Map<DrainagePipe, Double> pipeDiameters;
    private final List<DetentionFacility> detentionFacilities;
    private final DesignRainfall designRainfall;
    private final RegulatoryStandards regulations;
    private final WaterQualityRequirements waterQualityReq;
    private final List<DrainageNode> nodes;
    private double totalCost;
    public DrainageSolution(DesignRainfall designRainfall, RegulatoryStandards regulations,
                          WaterQualityRequirements waterQualityReq, List<DrainageNode> nodes) {
        this.pipeDiameters = new HashMap<>();
        this.detentionFacilities = new ArrayList<>();
        this.designRainfall = designRainfall;
        this.regulations = regulations;
        this.waterQualityReq = waterQualityReq;
        this.nodes = nodes;
        this.totalCost = 0.0;
    }
    public void addPipe(DrainagePipe pipe, double diameter) {
        pipeDiameters.put(pipe, diameter);
        totalCost += pipe.calculateInstallationCost(diameter);
    }
    public void addDetentionFacility(DetentionFacility facility) {
        detentionFacilities.add(facility);
        totalCost += facility.getCost();
    }
    public double calculateTotalRunoffVolume() {
        double totalVolume = 0.0;   
        for (Map.Entry<DrainagePipe, Double> entry : pipeDiameters.entrySet()) {
            DrainagePipe pipe = entry.getKey();
            DrainageNode fromNode = nodes.get(pipe.getFromNode());
            if (fromNode.getCatchment() != null) {
                double rainfallDepth = designRainfall.getIntensityForDuration(30) / 1000;
                double runoffVolume = fromNode.getCatchment().getRunoffCoefficient() * 
                                    rainfallDepth * 
                                    fromNode.getCatchment().getArea();
                totalVolume += runoffVolume;
            }
        }
        return totalVolume;
    }
    public double getTotalCost() {
        return totalCost;
    }
    public Set<DrainagePipe> getPipes() {
        return pipeDiameters.keySet();
    }
    public List<DetentionFacility> getDetentionFacilities() {
        return detentionFacilities;
    }
    public void printDetailedSolution() {
        System.out.println("=== Drainage Network Solution ===");
        System.out.println("Pipes:");
        System.out.printf("%-8s %-8s %-10s %-12s %-10s %-10s %-12s%n",
            "From", "To", "Length(m)", "Material", "Diameter(m)", "Slope(%)", "Cost");
        for (Map.Entry<DrainagePipe, Double> entry : pipeDiameters.entrySet()) {
            DrainagePipe pipe = entry.getKey();
            double diameter = entry.getValue();
            double slope = (nodes.get(pipe.getFromNode()).getElevation() - 
                          nodes.get(pipe.getToNode()).getElevation()) / pipe.getLength() * 100;
            double cost = pipe.calculateInstallationCost(diameter);
            System.out.printf("%-8d %-8d %-10.1f %-12s %-10.2f %-10.2f %-12.2f%n",
                pipe.getFromNode(), pipe.getToNode(), pipe.getLength(),
                pipe.getMaterial(), diameter, slope, cost);
        }
        if (!detentionFacilities.isEmpty()) {
            System.out.println("\nDetention Facilities:");
            System.out.printf("%-15s %-15s %-10s %-10s%n",
                "Name", "Type", "Capacity(m³)", "Cost");
            for (DetentionFacility facility : detentionFacilities) {
                System.out.printf("%-15s %-15s %-10.1f %-10.2f%n",
                    facility.getName(),
                    facility.isDetention() ? "Detention" : "Retention",
                    facility.getCapacity(),
                    facility.getCost());
            }
        }
    }
    public void printPerformanceMetrics() {
        System.out.printf("Total System Cost: $%.2f%n", totalCost);
        System.out.printf("Total Runoff Volume: %.2f m³%n", calculateTotalRunoffVolume());
        if (!detentionFacilities.isEmpty()) {
            double totalDetention = detentionFacilities.stream()
                .mapToDouble(DetentionFacility::getCapacity)
                .sum();
            System.out.printf("Total Detention Capacity: %.2f m³%n", totalDetention);
        }
        double totalCapacity = 0;
        double totalFlow = 0;
        for (Map.Entry<DrainagePipe, Double> entry : pipeDiameters.entrySet()) {
            DrainagePipe pipe = entry.getKey();
            double diameter = entry.getValue();
            double slope = (nodes.get(pipe.getFromNode()).getElevation() - 
                          nodes.get(pipe.getToNode()).getElevation()) / pipe.getLength();
            slope = Math.max(slope, regulations.getMinSlope() / 100.0);
            double radius = diameter / 2.0;
            double area = Math.PI * radius * radius;
            double hydraulicRadius = diameter / 4.0;
            double velocity = (1.0 / pipe.getMaterial().getManningCoefficient()) * 
                Math.pow(hydraulicRadius, 2.0/3.0) * Math.sqrt(slope);
            double capacity = area * velocity;
            double rainfallIntensity = designRainfall.getIntensityForDuration(30);
            double runoffCoefficient = nodes.get(pipe.getFromNode()).getCatchment() != null ?
                nodes.get(pipe.getFromNode()).getCatchment().getRunoffCoefficient() : 0.8;
            double areaDrained = nodes.get(pipe.getFromNode()).getCatchment() != null ?
                nodes.get(pipe.getFromNode()).getCatchment().getArea() : 0.0;
            double flow = runoffCoefficient * (rainfallIntensity / 1000 / 3600) * areaDrained;
            totalCapacity += capacity;
            totalFlow += flow;
        }
        double avgUtilization = totalCapacity > 0 ? (totalFlow / totalCapacity) * 100 : 0;
        System.out.printf("Average Pipe Utilization: %.1f%%%n", avgUtilization);
        double waterQualityScore = calculateWaterQualityScore();
        System.out.printf("Water Quality Score: %.1f/10%n", waterQualityScore);
    }
    private double calculateWaterQualityScore() {
        double score = 6.0;
        long hdpeCount = pipeDiameters.keySet().stream()
            .filter(pipe -> pipe.getMaterial() == PipeMaterial.HDPE)
            .count();
        score += hdpeCount * 0.1;
        if (!detentionFacilities.isEmpty()) {
            score += 2.0;
        }
        return Math.min(10.0, score);
    }
}