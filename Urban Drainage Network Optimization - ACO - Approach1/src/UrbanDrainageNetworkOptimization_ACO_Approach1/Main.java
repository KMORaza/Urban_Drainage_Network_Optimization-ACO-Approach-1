package UrbanDrainageNetworkOptimization_ACO_Approach1;
import java.util.*;
import java.util.stream.Collectors;
public class Main {
    private static final int NUM_ANTS = 50;
    private static final int MAX_ITERATIONS = 1000;
    private static final double EVAPORATION_RATE = 0.5;
    private static final double ALPHA = 1.0;
    private static final double BETA = 2.0;
    private static final double Q = 500;
    private static final double INITIAL_PHEROMONE = 0.1;
    private List<CatchmentArea> catchments;
    private List<DrainageNode> nodes;
    private List<DrainagePipe> pipes;
    private List<DetentionFacility> detentionFacilities;
    private double[][] pheromones;
    private double[][] heuristic;
    private Random random;
    private DesignRainfall designRainfall;
    private WaterQualityRequirements waterQualityReq;
    private RegulatoryStandards regulations;
    public static void main(String[] args) {
        Main optimizer = new Main();
        optimizer.initializeSystem();
        DrainageSolution bestSolution = optimizer.runACO();
        System.out.println("Optimal Solution Found:");
        bestSolution.printDetailedSolution();
        System.out.println("\nPerformance Metrics:");
        bestSolution.printPerformanceMetrics();
    }
    public void initializeSystem() {
        random = new Random();
        initializeEnvironmentalParameters();
        initializeCatchmentAreas();
        initializeDrainageNodes();
        initializePipes();
        initializeDetentionFacilities();
        initializePheromones();
        calculateHeuristicInformation();
    }
    private void initializeEnvironmentalParameters() {
        Map<Integer, Double> intensityDurationCurve = new HashMap<>();
        intensityDurationCurve.put(5, 120.0);
        intensityDurationCurve.put(15, 90.0);
        intensityDurationCurve.put(30, 70.0);
        intensityDurationCurve.put(60, 50.0);
        designRainfall = new DesignRainfall(10, intensityDurationCurve);
        waterQualityReq = new WaterQualityRequirements(50, 0.1, 6.0, 9.0, true);
        regulations = new RegulatoryStandards(3.0, 0.5, 0.7, 10);
    }
    private void initializeCatchmentAreas() {
        catchments = new ArrayList<>();
        catchments.add(new CatchmentArea(1, "Residential", 5000, 
            0.75, new Topography(102.5, 101.0, 2.5), 
            SoilType.URBAN, new LandUse(60, 30, 10)));
        catchments.add(new CatchmentArea(2, "Commercial", 8000, 
            0.85, new Topography(103.0, 101.5, 3.0), 
            SoilType.URBAN, new LandUse(80, 15, 5)));
        catchments.add(new CatchmentArea(3, "Industrial", 7000, 
            0.80, new Topography(103.5, 102.0, 3.5), 
            SoilType.URBAN, new LandUse(75, 10, 15)));
    }
    private void initializeDrainageNodes() {
        nodes = new ArrayList<>();
        nodes.add(new DrainageNode(0, "Outfall", 100.0, 0, null));
        for (CatchmentArea catchment : catchments) {
            nodes.add(new DrainageNode(
                    catchment.getId(),
                    catchment.getName(),
                catchment.getTopography().getElevation(),
                catchment.getArea(),
                    catchment));
        }
        nodes.add(new DrainageNode(4, "Junction1", 101.2, 0, null));
        nodes.add(new DrainageNode(5, "Junction2", 100.8, 0, null));
    }
    private void initializePipes() {
        pipes = new ArrayList<>();   
        addPipe(1, 4, 50, PipeMaterial.CONCRETE);
        addPipe(4, 5, 40, PipeMaterial.HDPE);
        addPipe(5, 0, 60, PipeMaterial.CONCRETE);
        addPipe(2, 4, 55, PipeMaterial.CONCRETE);
        addPipe(3, 5, 65, PipeMaterial.HDPE);
        addPipe(1, 5, 80, PipeMaterial.HDPE);
        addPipe(2, 5, 70, PipeMaterial.CONCRETE);
        addPipe(3, 4, 75, PipeMaterial.HDPE);
    }
    private void addPipe(int from, int to, double length, PipeMaterial material) {
        pipes.add(new DrainagePipe(from, to, length, material));
        pipes.add(new DrainagePipe(to, from, length, material));
    }
    private void initializeDetentionFacilities() {
        detentionFacilities = new ArrayList<>();
        detentionFacilities.add(new DetentionFacility(6, "Detention Basin", 1500, 0.5, 2.0, true));
        detentionFacilities.add(new DetentionFacility(7, "Retention Pond", 2000, 0.3, 1.5, false));
    }
    private void initializePheromones() {
        int numNodes = nodes.size();
        pheromones = new double[numNodes][numNodes];
        heuristic = new double[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            Arrays.fill(pheromones[i], INITIAL_PHEROMONE);
        }
    }
    private void calculateHeuristicInformation() {
        for (DrainagePipe pipe : pipes) {
            DrainageNode from = nodes.get(pipe.getFromNode());
            DrainageNode to = nodes.get(pipe.getToNode());
            double distanceFactor = 1.0 / pipe.getLength();
            double elevationFactor = calculateElevationFactor(from, to, pipe);
            double drainageAreaFactor = calculateDrainageAreaFactor(to);
            double floodRiskFactor = calculateFloodRiskFactor(from, to);
            double waterQualityFactor = calculateWaterQualityFactor(pipe);
            heuristic[pipe.getFromNode()][pipe.getToNode()] = 
                distanceFactor * elevationFactor * drainageAreaFactor * 
                floodRiskFactor * waterQualityFactor;
        }
    }
    private double calculateElevationFactor(DrainageNode from, DrainageNode to, DrainagePipe pipe) {
        double elevationDiff = from.getElevation() - to.getElevation();
        double minRequiredSlope = regulations.getMinSlope() / 100 * pipe.getLength();
        if (elevationDiff >= minRequiredSlope) {
            return 1.0 + (elevationDiff / pipe.getLength());
        }
        return 0.1;
    }
    private double calculateDrainageAreaFactor(DrainageNode node) {
        if (node.getCatchment() == null) return 1.0;
        return Math.log1p(node.getCatchment().getArea()) / 10.0;
    }
    private double calculateFloodRiskFactor(DrainageNode from, DrainageNode to) {
        return 1.0;
    }
    private double calculateWaterQualityFactor(DrainagePipe pipe) {
        if (pipe.getMaterial() == PipeMaterial.HDPE) return 1.1;
        return 1.0;
    }
    public DrainageSolution runACO() {
        DrainageSolution bestSolution = null;
        double bestCost = Double.MAX_VALUE;
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            List<DrainageSolution> antSolutions = new ArrayList<>();
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                DrainageSolution solution = constructSolution();
                antSolutions.add(solution);
                if (solution.getTotalCost() < bestCost) {
                    bestCost = solution.getTotalCost();
                    bestSolution = solution;
                }
            }
            updatePheromones(antSolutions);
            evaporatePheromones();
            if (iteration % 100 == 0) {
                System.out.printf("Iteration %d, Best cost: %.2f%n", iteration, bestCost);
            }
        }
        return bestSolution;
    }
    private DrainageSolution constructSolution() {
        DrainageSolution solution = new DrainageSolution(designRainfall, regulations, waterQualityReq, nodes);
        for (CatchmentArea catchment : catchments) {
            int startNodeId = catchment.getId();
            List<Integer> path = findPathToOutfall(startNodeId);
            addPathToSolution(solution, path);
        }
        evaluateDetentionRequirements(solution);
        return solution;
    }
    private List<Integer> findPathToOutfall(int startNodeId) {
        List<Integer> path = new ArrayList<>();
        path.add(startNodeId);
        int currentNodeId = startNodeId;
        while (currentNodeId != 0) {
            int nextNodeId = selectNextNode(currentNodeId, path);
            path.add(nextNodeId);
            currentNodeId = nextNodeId;
        }
        return path;
    }
    private int selectNextNode(int currentNodeId, List<Integer> path) {
        List<Integer> possibleNodes = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();
        double total = 0.0;
        for (DrainagePipe pipe : pipes) {
            if (pipe.getFromNode() == currentNodeId && !path.contains(pipe.getToNode())) {
                possibleNodes.add(pipe.getToNode());
                double pheromone = Math.pow(pheromones[currentNodeId][pipe.getToNode()], ALPHA);
                double heuristicValue = Math.pow(heuristic[currentNodeId][pipe.getToNode()], BETA);
                double probability = pheromone * heuristicValue;
                probabilities.add(probability);
                total += probability;
            }
        }
        if (possibleNodes.isEmpty()) {
            for (DrainagePipe pipe : pipes) {
                if (pipe.getFromNode() == currentNodeId) {
                    possibleNodes.add(pipe.getToNode());
                    double pheromone = Math.pow(pheromones[currentNodeId][pipe.getToNode()], ALPHA);
                    double heuristicValue = Math.pow(heuristic[currentNodeId][pipe.getToNode()], BETA);
                    double probability = pheromone * heuristicValue;
                    probabilities.add(probability);
                    total += probability;
                }
            }
        }
        double rand = random.nextDouble() * total;
        double cumulative = 0.0;
        for (int i = 0; i < possibleNodes.size(); i++) {
            cumulative += probabilities.get(i);
            if (rand <= cumulative) {
                return possibleNodes.get(i);
            }
        }
        return possibleNodes.get(possibleNodes.size() - 1);
    }
    private void addPathToSolution(DrainageSolution solution, List<Integer> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            int fromNodeId = path.get(i);
            int toNodeId = path.get(i + 1);
            DrainagePipe pipe = findPipe(fromNodeId, toNodeId);
            if (pipe != null) {
                DrainageNode fromNode = nodes.get(fromNodeId);
                DrainageNode toNode = nodes.get(toNodeId);
                double diameter = calculateRequiredDiameter(pipe, fromNode, toNode);
                solution.addPipe(pipe, diameter);
            }
        }
    }
    private DrainagePipe findPipe(int fromNodeId, int toNodeId) {
        for (DrainagePipe pipe : pipes) {
            if (pipe.getFromNode() == fromNodeId && pipe.getToNode() == toNodeId) {
                return pipe;
            }
        }
        return null;
    }
    private double calculateRequiredDiameter(DrainagePipe pipe, DrainageNode from, DrainageNode to) {
        double rainfallIntensity = designRainfall.getIntensityForDuration(30);
        double runoffCoefficient = from.getCatchment() != null ? 
            from.getCatchment().getRunoffCoefficient() : 0.8;
        double area = from.getCatchment() != null ? 
            from.getCatchment().getArea() : 0.0;
        double peakFlow = runoffCoefficient * (rainfallIntensity / 1000 / 3600) * area;
        double slope = (from.getElevation() - to.getElevation()) / pipe.getLength();
        slope = Math.max(slope, regulations.getMinSlope() / 100.0);
        double manningN = pipe.getMaterial().getManningCoefficient();
        for (double diameter : new double[]{0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5}) {
            double radius = diameter / 2.0;
            double areaFlow = Math.PI * radius * radius;
            double hydraulicRadius = diameter / 4.0;
            double velocity = (1.0 / manningN) * 
                Math.pow(hydraulicRadius, 2.0/3.0) * 
                Math.sqrt(slope);
            if (velocity > regulations.getMaxVelocity()) continue;
            double capacity = areaFlow * velocity;
            if (capacity >= peakFlow) {
                return diameter;
            }
        }
        return 1.5;
    }
    private void evaluateDetentionRequirements(DrainageSolution solution) {
        double totalVolume = solution.calculateTotalRunoffVolume();
        double detentionRequired = totalVolume * 0.2;
        DetentionFacility bestFacility = null;
        double bestCostPerVolume = Double.MAX_VALUE;
        for (DetentionFacility facility : detentionFacilities) {
            if (facility.getCapacity() >= detentionRequired) {
                double costPerVolume = facility.getCost() / facility.getCapacity();
                if (costPerVolume < bestCostPerVolume) {
                    bestCostPerVolume = costPerVolume;
                    bestFacility = facility;
                }
            }
        }
        if (bestFacility != null) {
            solution.addDetentionFacility(bestFacility);
        }
    }
    private void updatePheromones(List<DrainageSolution> solutions) {
        for (DrainageSolution solution : solutions) {
            double solutionQuality = Q / solution.getTotalCost();
            for (DrainagePipe pipe : solution.getPipes()) {
                pheromones[pipe.getFromNode()][pipe.getToNode()] += solutionQuality;
            }
        }
    }
    private void evaporatePheromones() {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= (1.0 - EVAPORATION_RATE);
                if (pheromones[i][j] < 0.0001) {
                    pheromones[i][j] = 0.0001;
                }
            }
        }
    }
}