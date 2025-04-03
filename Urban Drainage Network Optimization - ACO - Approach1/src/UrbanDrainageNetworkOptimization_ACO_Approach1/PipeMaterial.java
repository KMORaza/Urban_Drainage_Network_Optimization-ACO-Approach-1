package UrbanDrainageNetworkOptimization_ACO_Approach1;
public enum PipeMaterial {
    CONCRETE(0.013, "Concrete"),
    HDPE(0.009, "High Density Polyethylene"),
    PVC(0.010, "Polyvinyl Chloride"),
    CORRUGATED_METAL(0.024, "Corrugated Metal");
    private final double manningCoefficient;
    private final String displayName;
    PipeMaterial(double manningCoefficient, String displayName) {
        this.manningCoefficient = manningCoefficient;
        this.displayName = displayName;
    }
    public double getManningCoefficient() {
        return manningCoefficient;
    }
    public String getDisplayName() {
        return displayName;
    }
    public double getCostPerMeter(double diameter) {
        switch (this) {
            case CONCRETE:
                return 50 + 100 * diameter * diameter;
            case HDPE:
                return 70 + 150 * diameter * diameter;
            case PVC:
                return 60 + 130 * diameter * diameter;
            case CORRUGATED_METAL:
                return 80 + 120 * diameter * diameter;
            default:
                return 100 + 200 * diameter * diameter;
        }
    }
}